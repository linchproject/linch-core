package com.linchproject.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple IOC container that autowires by setter injection.
 *
 * @author Georg Schmidl
 */
public class Container {

    private static Container instance;

    private Map<String, Class<?>> classes;
    private Map<String, Object> objects;

    protected Container() {
        this.classes = new HashMap<String, Class<?>>();
        this.objects = new HashMap<String, Object>();
    }

    /**
     * Adds a component to the container by given key.
     *
     * The setter method that will be called to inject the
     * instance of this component must match the key.
     *
     * For example, if your key is "myComponent", the
     * container will look for a setter "setMyComponent".
     *
     * @param key the unique component key
     * @param clazz the component class
     */
    public void add(String key, Class<?> clazz) {
        this.classes.put(key, clazz);
    }

    /**
     * Returns the component instance for given key.
     *
     * @param key the unique component key
     * @return instance of the component
     */
    public Object get(String key) {
        Object object = this.objects.get(key);
        if (object == null) {
            Class<?> clazz = classes.get(key);
            if (clazz != null) {
                try {
                    object = clazz.newInstance();
                    this.objects.put(key, object);
                    autowire(object);
                } catch (ReflectiveOperationException e) {
                    // ignore
                }
            }
        }
        return object;
    }

    /**
     * Autowires a given objects by setter injection.
     *
     * @param object object to be autowired
     */
    public void autowire(Object object) {
        for (Map.Entry<String, Class<?>> entry : this.classes.entrySet()) {
            String key = entry.getKey();
            Class<?> clazz = entry.getValue();
            try {
                Method method = object.getClass().getMethod(getSetterName(key), clazz);
                method.invoke(object, get(key));
            } catch (ReflectiveOperationException e) {
                // ignore
            }
        }
    }

    private String getSetterName(String key) {
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1, key.length());
    }

    public static Container getInstance() {
        if (instance == null) {
            instance = new Container();
        }
        return instance;
    }
}
