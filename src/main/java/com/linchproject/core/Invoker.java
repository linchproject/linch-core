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
    private String controllerPackage;
    private Container container;

    public Invoker(ClassLoader classLoader, String controllerPackage, Container container) {
        this.classLoader = classLoader;
        this.controllerPackage = controllerPackage;
        this.container = container;
    }

    public Result invoke(Route route) {
        String subPackage = route.getSubPackage();
        String controller = route.getController();
        String action = route.getAction();
        Map<String, String[]> parameterMap = route.getParameterMap();

        Result result;

        try {
            String controllerClassName = getControllerClassName(controller, subPackage);
            Class<?> controllerClass = this.classLoader.loadClass(controllerClassName);
            Object controllerInstance = controllerClass.newInstance();

            this.container.autowire(controllerInstance);

            Method setRouteMethod = controllerClass.getMethod("setRoute", Route.class);
            setRouteMethod.invoke(controllerInstance, route);

            try {
                Method actionMethod;

                try {
                    actionMethod = controllerClass.getMethod("_all", Params.class);
                } catch (NoSuchMethodException e) {
                    actionMethod = controllerClass.getMethod(action, Params.class);
                }
                result = (Result) actionMethod.invoke(controllerInstance, new Params(parameterMap));

                if (result instanceof Dispatch) {
                    return invoke(((Dispatch) result).getRoute());
                }

            } catch (NoSuchMethodException e) {
                result = new Error("Cannot find action '" + action + "' in controller '" + controller + "'");
            } catch (IllegalAccessException e) {
                result = new Error("Cannot access action '" + action + "' in controller '" + controller + "'", e);
            } catch (InvocationTargetException e) {
                result = new Error("Cannot invoke action '" + action + "' in controller '" + controller + "'", e);
            }

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
        return controllerPackage
                + (subPackage != null? "." + subPackage: "")
                + "."
                + controller.substring(0, 1).toUpperCase()
                + controller.substring(1, controller.length())
                + "Controller";
    }
}
