package com.linchproject.core;

/**
 * @author Georg Schmidl
 */
public abstract class Route {

    private static final String DEFAULT_CONTROLLER = "app";
    private static final String DEFAULT_ACTION = "index";

    private String controller;
    private String action;
    private Params params;

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

    public Params getParams() {
        return params == null? Params.getEmptyParams(): params;
    }

    public void setParams(Params params) {
        this.params = params;
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
