package com.linchproject.core;

import com.linchproject.core.results.Dispatch;
import com.linchproject.core.results.Error;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
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
        return new Dispatcher().dispatch(route);
    }

    public class Dispatcher {

        private Map<String, List<String>> controllerHistory = new HashMap<String, List<String>>();
        private Map<Class<?>, Object> controllerInstances = new LinkedHashMap<Class<?>, Object>();

        public Result dispatch(Route route) {
            Result result = null;
            Exception exception = null;

            String controllerPackage = route.getControllerPackage();
            String controller;
            String action;

            try {
                if (!controllerHistory.keySet().contains(controllerPackage)) {
                    controller = "_";
                    action = "_";
                    controllerHistory.put(controllerPackage, new ArrayList<String>());
                } else {
                    if (!controllerHistory.get(controllerPackage).contains(route.getController())) {
                        controller = route.getController();
                        action = "_";
                        controllerHistory.get(controllerPackage).add(route.getController());
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
            } catch (Exception e) {
                result = new Error(e.getMessage(), e);
                exception = e;
            }

            if (result == null) {
                result = dispatch(route);
            } else if (result instanceof Dispatch) {
                result = dispatch(((Dispatch) result).getRoute());
            } else {
                for (Object controllerInstance : controllerInstances.values()) {
                    try {
                        quit(controllerInstance, exception);
                    } catch (InvocationException e) {
                        result = new Error(e.getMessage(), e);
                        exception = e;
                    }
                }
            }

            return result;
        }

        private void quit(Object controllerInstance, Exception exception) throws InvocationException {
            try {
                Method method = controllerInstance.getClass().getMethod("_quit", Exception.class);
                method.invoke(controllerInstance, exception);
            } catch (NoSuchMethodException e) {
                // ok
            } catch (IllegalAccessException e) {
                // ok
            } catch (InvocationTargetException e) {
                throw new InvocationException("Error invoking'" + controllerInstance.getClass().getName() + "#" + "_quit" + "'", e);
            }
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
                // ok
            } catch (NoSuchMethodException e) {
                // ok
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

            public void setControllerClass(Class<?> controllerClass) {
                this.controllerClass = controllerClass;
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

            public Result invoke(Route route) throws InvocationException {
                Result result;

                try {
                    boolean init = false;
                    Object controllerInstance = controllerInstances.get(controllerClass);
                    if (controllerInstance == null) {
                        controllerInstance = controllerClass.newInstance();
                        controllerInstances.put(controllerClass, controllerInstance);
                        init = true;
                    }

                    try {
                        Method setRouteMethod = controllerClass.getMethod("setRoute", Route.class);
                        setRouteMethod.invoke(controllerInstance, route);
                    } catch (NoSuchMethodException e) {
                        // ok
                    } catch (IllegalAccessException e) {
                        // ok
                    } catch (InvocationTargetException e) {
                        throw new InvocationException("Error invoking'" + controllerClass.getName() + "#" + "_setRoute" + "'", e);
                    }

                    if (injector != null) {
                        injector.inject(controllerInstance);
                    }

                    if (init) {
                        init(controllerInstance);
                    }

                    try {
                        result = (Result) actionMethod.invoke(controllerInstance, new Params(route.getParameterMap()));
                    } catch (IllegalAccessException e) {
                        throw new InvocationException("Cannot access '" + controllerClass.getName() + "#" + actionMethod.getName() + "'", e);
                    } catch (InvocationTargetException e) {
                        throw new InvocationException("Error invoking '" + controllerClass.getName() + "#" + actionMethod.getName() + "'", e);
                    }

                } catch (InstantiationException e) {
                    throw new InvocationException("Cannon instantiate '" + controllerClass.getName() + "'", e);
                } catch (IllegalAccessException e) {
                    throw new InvocationException("Cannot access '" + controllerClass.getName() + "'", e);
                }

                return result;
            }

            private void init(Object controllerInstance) throws InvocationException {
                try {
                    Method setRouteMethod = controllerClass.getMethod("_init");
                    setRouteMethod.invoke(controllerInstance);
                } catch (NoSuchMethodException e) {
                    // ok
                } catch (IllegalAccessException e) {
                    // ok
                } catch (InvocationTargetException e) {
                    throw new InvocationException("Error invoking'" + controllerInstance.getClass().getName() + "#" + "_init" + "'", e);
                }
            }
        }
    }

    public class InvocationException extends Exception {
        public InvocationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
