package com.linchproject.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public abstract class Route {

    private static final String DEFAULT_CONTROLLER = "app";
    private static final String DEFAULT_ACTION = "index";

    private String subPackage;
    private String controller;
    private String action;
    private String tail;
    private Map<String, String[]> parameterMap;

    public String getSubPackage() {
        return subPackage;
    }

    public void setSubPackage(String subPackage) {
        this.subPackage = subPackage;
    }

    public String getController() {
        return controller == null? DEFAULT_CONTROLLER: controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getAction() {
        return action == null? DEFAULT_ACTION: action;
    }

    public String getTail() {
        return tail;
    }

    public void setTail(String tail) {
        this.tail = tail;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Map<String, String[]> getParameterMap() {
        return parameterMap == null? new HashMap<String, String[]>(): parameterMap;
    }

    public void setParameterMap(Map<String, String[]> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Route copy() {
        Route route = newRoute();
        route.subPackage = this.subPackage;
        route.controller = this.controller;
        route.action = this.action;
        route.tail = this.tail;
        route.parameterMap = this.parameterMap;
        return route;
    }

    public Route shift(String subPackage) {
        Route route = newRoute();

        route.subPackage = this.subPackage != null? this.subPackage + "." + subPackage: subPackage;
        route.controller = this.action;

        if (this.tail != null) {
            String[] tailSplit = this.tail.split("/", 2);
            route.action = tailSplit[0];
            if (tailSplit.length > 1 && tailSplit[1].length() > 0) {
                route.tail = tailSplit[1];
            } else {
                route.tail = null;
            }
        }

        route.parameterMap = this.parameterMap;
        return route;
    }

    protected abstract Route newRoute();

    public abstract String getUrl();
}
