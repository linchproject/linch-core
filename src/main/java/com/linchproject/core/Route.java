package com.linchproject.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public abstract class Route {

    private static final String DEFAULT_CONTROLLER = "app";
    private static final String DEFAULT_ACTION = "index";

    private String controller;
    private String action;
    private Map<String, String[]> parameterMap;

    public String getController() {
        return controller == null? DEFAULT_CONTROLLER: controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getAction() {
        return action == null? DEFAULT_ACTION: action;
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

    public abstract String getUrl();

    public Route createRoute() {
        Route route = newRoute();
        route.setController(this.controller);
        route.setAction(this.action);
        return route;
    }

    protected abstract Route newRoute();
}
