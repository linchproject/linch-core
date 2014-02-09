package com.linchproject.core;

/**
 * @author Georg Schmidl
 */
public class Route {

    private static Route defaultRoute;

    private static final String DEFAULT_CONTROLLER = "app";
    private static final String DEFAULT_ACTION = "index";

    private String controller;
    private String action;
    private Params params;

    public Route() {
        this(null, null);
    }

    public Route(String controller, String action) {
        this(controller, action, null);
    }

    public Route(String controller, String action, Params params) {
        this.controller = controller;
        this.action = action;
        this.params = params;
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

    public void setAction(String action) {
        this.action = action;
    }

    public Params getParams() {
        return params == null? Params.getEmptyParams(): params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public static Route getDefaultRoute() {
        if (defaultRoute == null) {
            defaultRoute = new Route();
        }
        return defaultRoute;
    }
}
