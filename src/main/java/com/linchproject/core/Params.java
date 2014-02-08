package com.linchproject.core;

import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Params {

    private Map<String, String[]> map;

    public Params(Map<String, String[]> map) {
        this.map = map;
    }

    public String get(String key) {
        String[] value = map.get(key);
        return value != null && value.length > 0? value[0]: null;
    }

    public String[] getAll(String key) {
        return map.get(key);
    }

    public Map<String, String[]> getMap() {
        return map;
    }
}
