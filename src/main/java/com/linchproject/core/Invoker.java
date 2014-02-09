package com.linchproject.core;

import com.linchproject.core.results.Error;

import java.lang.reflect.Method;

/**
 * @author Georg Schmidl
 */
public class Invoker {

    private ClassLoader classLoader;
    private String controllerPackage;

    public Invoker(ClassLoader classLoader, String controllerPackage) {
        this.classLoader = classLoader;
        this.controllerPackage = controllerPackage;
    }

    public Result invoke(Route route) {
        String controller = route.getController();
        String action = route.getAction();
        Params params = route.getParams();

        Result result;

        try {
            Class<?> controllerClass = this.classLoader.loadClass(getControllerClassName(controller));
            Object controllerInstance = controllerClass.newInstance();

            Container.getInstance().autowire(controllerInstance);

            Method actionMethod = controllerClass.getMethod(action, Params.class);
            result = (Result) actionMethod.invoke(controllerInstance, params);

        } catch (ReflectiveOperationException e) {
            result = new Error(e);
        }

        return result;
    }

    private String getControllerClassName(String controller) {
        return controllerPackage
                + "."
                + controller.substring(0, 1).toUpperCase()
                + controller.substring(1, controller.length())
                + "Controller";
    }
}
