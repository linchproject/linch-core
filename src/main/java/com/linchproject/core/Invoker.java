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

    private Set<String> controllerBlackList = new HashSet<String>();

    public Invoker(ClassLoader classLoader) {
        this(classLoader, null);
    }

    public Invoker(ClassLoader classLoader, Injector injector) {
        this.classLoader = classLoader;
        this.injector = injector;
    }

    public Result invoke(Route route) {
        return new InternalInvoker().invoke(route);
    }

    public class InternalInvoker {

        private Map<String, Set<String>> controllerHistory = new HashMap<String, Set<String>>();
        private Map<Class<?>, Object> controllerInstances = new LinkedHashMap<Class<?>, Object>();

        public Result invoke(Route route) {
            Result result = null;

            String controllerPackage = route.getControllerPackage();
            String controller;
            String action;

            try {
                if (!controllerHistory.keySet().contains(controllerPackage)) {
                    controller = "_";
                    action = "_";
                    controllerHistory.put(controllerPackage, new HashSet<String>());
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

                String controllerClassName = getControllerClassName(controllerPackage, controller);

                if (!"_".equals(action) || !controllerBlackList.contains(controllerClassName)) {
                    Invocation invocation = getInvocation(controllerClassName, action);
                    if (invocation.canInvoke()) {
                        result = invocation.invoke(route);

                    } else if (!invocation.controllerExists()) {
                        if (!"_".equals(controller)) {
                            result = new Error("Cannot find controller '" + route.getController() + "'");
                        } else {
                            controllerBlackList.add(controllerClassName);
                        }
                    } else if (!invocation.actionExists()) {
                        if (!"_".equals(action)) {
                            result = new Error("Cannot find action '" + route.getAction() + "'");
                        } else {
                            controllerBlackList.add(controllerClassName);
                        }
                    }
                }

            } catch (Exception e) {
                result = new Error(e.getMessage(), e);
            }

            if (result == null) {
                result = invoke(route);
            } else if (result instanceof Dispatch) {
                result = invoke(((Dispatch) result).getRoute());
            }

            return result;
        }

        private Invocation getInvocation(String controllerClassName, String action) {
            Invocation invocation = new Invocation();

            try {
                Class<?> controllerClass = classLoader.loadClass(controllerClassName);
                invocation.setControllerClass(controllerClass);
                Method actionMethod = controllerClass.getMethod(action + "Action");
                invocation.setActionMethod(actionMethod);
            } catch (ClassNotFoundException e) {
                // ok
            } catch (NoSuchMethodException e) {
                // ok
            }
            return invocation;
        }

        private String getControllerClassName(String controllerPackage, String controller) {
            return (controllerPackage != null? controllerPackage + ".": "")
                    + controller.substring(0, 1).toUpperCase()
                    + controller.substring(1, controller.length())
                    + "Controller";
        }

        private void invokeMethod(Object controllerInstance, String methodName) throws InvocationException {
            try {
                Method method = controllerInstance.getClass().getMethod(methodName);
                method.invoke(controllerInstance);
            } catch (NoSuchMethodException e) {
                // ok
            } catch (IllegalAccessException e) {
                // ok
            } catch (InvocationTargetException e) {
                throw new InvocationException("Error invoking'" + controllerInstance.getClass().getName() + "#" + methodName + "'", e);
            }
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
                    Object controllerInstance = controllerInstances.get(controllerClass);
                    if (controllerInstance == null) {
                        controllerInstance = controllerClass.newInstance();
                        controllerInstances.put(controllerClass, controllerInstance);
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
                        try {
                            injector.inject(controllerInstance);
                        } catch (Exception e) {
                            throw new InvocationException("Error injecting '" + controllerClass.getName() + "'", e);
                        }
                    }

                    try {
                        result = (Result) actionMethod.invoke(controllerInstance);
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
        }
    }

    public class InvocationException extends Exception {
        public InvocationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
