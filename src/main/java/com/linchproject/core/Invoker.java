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
    private Injector injector;

    public Invoker(ClassLoader classLoader) {
        this(classLoader, null);
    }

    public Invoker(ClassLoader classLoader, Injector injector) {
        this.classLoader = classLoader;
        this.injector = injector;
    }

    public Result invoke(Route route) {
        return dispatch(route, true, true);
    }

    public Result dispatch(Route route, boolean invoke_Controller, boolean invoke_Method) {
        Result result = null;

        String controllerPackage = route.getControllerPackage();
        String controller;
        String action;

        if (invoke_Controller) {
            controller = "_";
            action = "_";
            invoke_Controller = false;
        } else {
            if (invoke_Method) {
                controller = route.getController();
                action = "_";
                invoke_Method = false;
            } else {
                controller = route.getController();
                action = route.getAction();
            }
        }

        Invocation invocation = getInvocation(controllerPackage, controller, action);
        if (invocation.canInvoke()) {
            result = invocation.invoke(route);
        } else if (!"_".equals(controller)  && !invocation.controllerExists()) {
            result = new Error("Cannot find controller '" + route.getController() + "'");
        } else if (!"_".equals(action) && !invocation.actionExists()) {
            result = new Error("Cannot find action '" + route.getAction() + "'");
        }

        if (result == null) {
            result = dispatch(route, false, invoke_Method);
        } else if (result instanceof Dispatch) {
            Route newRoute = ((Dispatch) result).getRoute();
            boolean isSamePackage = route.isSamePackage(newRoute);
            result = dispatch(newRoute, !isSamePackage || invoke_Controller, !isSamePackage || invoke_Method);
        }

        return result;
    }


    private Invocation getInvocation(String controllerPackage, String controller, String action) {
        Invocation invocation = new Invocation();

        String controllerClassName = getControllerClassName(controller, controllerPackage);
        try {
            Class<?> controllerClass = classLoader.loadClass(controllerClassName);
            invocation.setControllerClass(controllerClass);
            Method actionMethod = controllerClass.getMethod(action, Params.class);
            invocation.setActionMethod(actionMethod);
        } catch (ClassNotFoundException e) {
            // ignore
        } catch (NoSuchMethodException e) {
            // ignore
        }
        return invocation;
    }

    private String getControllerClassName(String controller, String controllerPackage) {
        return (controllerPackage != null? controllerPackage + ".": "")
                + controller.substring(0, 1).toUpperCase()
                + controller.substring(1, controller.length())
                + "Controller";
    }

    public class Invocation {
        private Class<?> controllerClass;
        private Method actionMethod;

        public Class<?> getControllerClass() {
            return controllerClass;
        }

        public void setControllerClass(Class<?> controllerClass) {
            this.controllerClass = controllerClass;
        }

        public Method getActionMethod() {
            return actionMethod;
        }

        public void setActionMethod(Method actionMethod) {
            this.actionMethod = actionMethod;
        }

        boolean controllerExists() {
            return controllerClass != null;
        }

        boolean actionExists() {
            return actionMethod != null;
        }

        boolean canInvoke() {
            return controllerExists() && actionExists();
        }

        public Result invoke(Route route) {
            Result result;

            try {
                Object controllerInstance = controllerClass.newInstance();

                try {
                    Method setRouteMethod = controllerClass.getMethod("setRoute", Route.class);
                    setRouteMethod.invoke(controllerInstance, route);
                } catch (NoSuchMethodException e) {
                    // we tried
                } catch (IllegalAccessException e) {
                    // we tried
                } catch (InvocationTargetException e) {
                    // we tried
                }

                if (injector != null) {
                    injector.inject(controllerInstance);
                }

                try {
                    result = (Result) actionMethod.invoke(controllerInstance, new Params(route.getParameterMap()));
                } catch (IllegalAccessException e) {
                    result = new Error("Cannot access '" + controllerClass.getName() + "#" + actionMethod.getName() + "'", e);
                } catch (InvocationTargetException e) {
                    result = new Error("Cannot invoke '" + controllerClass.getName() + "#" + actionMethod.getName() + "'", e);
                }

            } catch (InstantiationException e) {
                result = new Error("Cannon instantiate '" + controllerClass.getName() + "'", e);
            } catch (IllegalAccessException e) {
                result = new Error("Cannot access '" + controllerClass.getName() + "'", e);
            }

            return result;
        }

        public class ControllerIllegalAccessException extends IllegalAccessException {
            public ControllerIllegalAccessException() {
            }

            public ControllerIllegalAccessException(String s) {
                super(s);
            }
        }
    }
}
