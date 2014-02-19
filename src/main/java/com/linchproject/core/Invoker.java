package com.linchproject.core;

import com.linchproject.core.results.Dispatch;
import com.linchproject.core.results.Error;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Invoker {

    private ClassLoader classLoader;
    private String controllersPackage;
    private Injector injector;

    public Invoker(ClassLoader classLoader, String controllersPackage) {
        this(classLoader, controllersPackage, null);
    }

    public Invoker(ClassLoader classLoader, String controllersPackage, Injector injector) {
        this.classLoader = classLoader;
        this.controllersPackage = controllersPackage;
        this.injector = injector;
    }

    public Result invoke(Route route) {
        String subPackage = route.getSubPackage();
        String controller = route.getController();
        Map<String, String[]> parameterMap = route.getParameterMap();

        Result result;

        try {
            String controllerClassName = getControllerClassName(controller, subPackage);
            Class<?> controllerClass = this.classLoader.loadClass(controllerClassName);

            Object controllerInstance = controllerClass.newInstance();

            Method setRouteMethod = controllerClass.getMethod("setRoute", Route.class);
            setRouteMethod.invoke(controllerInstance, route);

            if (injector != null) {
                injector.inject(controllerInstance);
            }

            Method initMethod = controllerClass.getMethod("_init");
            Method quitMethod = controllerClass.getMethod("_quit", Exception.class);
            Method filterMethod = controllerClass.getMethod("_filter", Params.class);

            initMethod.invoke(controllerInstance);

            Result filterResult;

            try {
                filterResult = (Result) filterMethod.invoke(controllerInstance, new Params(parameterMap));
            } catch (Exception e){
                quitMethod.invoke(controllerInstance, e);
                throw e;
            }

            if (filterResult instanceof Dispatch) {
                Route newRoute = ((Dispatch) filterResult).getRoute();
                String newSubPackage = newRoute.getSubPackage();
                String newController = newRoute.getController();
                String newControllerClassName = getControllerClassName(newController, newSubPackage);

                if (!controllerClassName.equals(newControllerClassName)) {
                    result = invoke(newRoute);

                } else {
                    String action = newRoute.getAction();

                    try {
                        Method actionMethod = controllerClass.getMethod(action, Params.class);

                        try {
                            result = (Result) actionMethod.invoke(controllerInstance, new Params(parameterMap));

                        } catch (Exception e){
                            quitMethod.invoke(controllerInstance, e);
                            throw e;
                        }

                        if (result instanceof Dispatch) {
                            result = invoke(((Dispatch) result).getRoute());
                        }
                    } catch (NoSuchMethodException e) {
                        quitMethod.invoke(controllerInstance, e);
                        result = new Error("Cannot find action '" + action + "' in controller '" + controller + "'");
                    } catch (IllegalAccessException e) {
                        quitMethod.invoke(controllerInstance, e);
                        result = new Error("Cannot access action '" + action + "' in controller '" + controller + "'", e);
                    } catch (InvocationTargetException e) {
                        quitMethod.invoke(controllerInstance, e);
                        result = new Error("Cannot invoke action '" + action + "' in controller '" + controller + "'", e);
                    }
                }
            } else {
                result = filterResult;
            }
            quitMethod.invoke(controllerInstance, new Exception[]{null});

        } catch (ClassNotFoundException e) {
            result = new Error("Cannot find controller '" + controller + "'");
        } catch (IllegalAccessException e) {
            result = new Error("Cannot access controller '" + controller + "'", e);
        } catch (InstantiationException e) {
            result = new Error("Cannot instantiate controller '" + controller + "'", e);

        } catch (Exception e) {
            result = new Error("An error occurred", e);
        }

        return result;
    }

    private String getControllerClassName(String controller, String subPackage) {
        return controllersPackage
                + (subPackage != null? "." + subPackage: "")
                + "."
                + controller.substring(0, 1).toUpperCase()
                + controller.substring(1, controller.length())
                + "Controller";
    }
}
