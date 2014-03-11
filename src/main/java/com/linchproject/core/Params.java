package com.linchproject.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Object for simple access of request parameters.
 *
 * @author Georg Schmidl
 */
public class Params {

    private Map<String, String[]> map;

    public Params() {
        this(new HashMap<String, String[]>());
    }

    public Params(Map<String, String[]> map) {
        this.map = map;
    }

    /**
     * Returns the first value for a given key.
     *
     * @param key the parameter key
     * @return the parameter value
     */
    public String get(String key) {
        String[] value = map.get(key);
        return value != null && value.length > 0? value[0]: null;
    }

    /**
     * Returns all values for a given key.
     *
     * @param key the parameter key
     * @return the parameter values
     */
    public String[] getAll(String key) {
        return map.get(key);
    }

    /**
     * Returns the parameters as map.
     *
     * @return the parameter mal
     */
    public Map<String, String[]> getMap() {
        return map;
    }
}
