package com.linchproject.core;

import com.linchproject.core.results.Dispatch;
import com.linchproject.core.results.Error;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Pseudo Code:
 *
 * invoke(route)
 *   dispatch(route, true, true)
 *
 * dispatch(route, call_controller, call_method)
 *   if (call_controller)
 *     result = invoke(_controller, _method)
 *     call_controller = false;
 *   else
 *     if (call_method)
 *       result = invoke(route.contoller, _method)
 *       call_method = false
 *     else
 *       result = invoke(route.controller, route.action)
 *
 *   if (result == dispatch)
 *     dispatch(result.route, false, true)
 *
 *   return result
 *
 *
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
        String action = route.getAction();
        Params params = new Params(route.getParameterMap());

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

            initMethod.invoke(controllerInstance);

            try {
                Method _Method = controllerClass.getMethod("_", Params.class);

                Result _Result;

                try {
                    _Result = (Result) _Method.invoke(controllerInstance, params);
                } catch (Exception e){
                    quitMethod.invoke(controllerInstance, e);
                    throw e;
                }

                if (_Result instanceof Dispatch) {
                    Route _Route = ((Dispatch) _Result).getRoute();
                    String _SubPackage = _Route.getSubPackage();
                    String _Controller = _Route.getController();
                    String _ControllerClassName = getControllerClassName(_Controller, _SubPackage);

                    if (!controllerClassName.equals(_ControllerClassName)) {
                        result = invoke(_Route);

                    } else {
                        action = _Route.getAction();
                        result = invokeAction(action, controller, params, controllerClass, controllerInstance, quitMethod);
                    }
                } else {
                    result = _Result;
                }
            } catch (NoSuchMethodException e) {
                result = invokeAction(action, controller, params, controllerClass, controllerInstance, quitMethod);
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

    private Result invokeAction(String action, String controller, Params params, Class<?> controllerClass, Object controllerInstance, Method quitMethod) throws Exception {
        Result result;
        try {
            Method actionMethod = controllerClass.getMethod(action, Params.class);

            try {
                result = (Result) actionMethod.invoke(controllerInstance, params);

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
