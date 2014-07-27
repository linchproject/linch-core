package com.linchproject.core;

import java.util.*;

/**
 * A route contains a controller package and a path. Both together define the
 * action that will be invoked. The package defines the location of the
 * controllers. The controller and the action will be determined from the path,
 * which is divided into several tokens, separated by slashes. Also a current
 * position in the path is maintained by the route. From this position, the
 * next token is the name of the controller and the token after that the name
 * of the action.
 *
 * @author Georg Schmidl
 */
public abstract class Route {

    private static final String DEFAULT_CONTROLLER = "app";
    private static final String DEFAULT_ACTION = "index";

    private String controllerPackage;
    private int packageRoot = -1;

    private String path;
    private int cursor;

    protected Route() {
        setPath("/");
    }

    protected Route(String path) {
        setPath(path);
    }

    protected Route(String path, String controllerPackage) {
        setPath(path);
        setControllerPackage(controllerPackage);
    }

    protected abstract Route newRoute();

    protected void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
        this.packageRoot = this.controllerPackage != null ? controllerPackage.length() : -1;
    }

    protected void setPath(String path) {
        this.path = normalize(path);
        this.cursor = 0;
    }

    /**
     * Removes trailing slashes and trialing default actions or controllers.
     * Also adds a leading slash if it is missing.
     *
     * @param path path to be normalized
     * @return the normalized path
     */
    private static String normalize(String path) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        String defaultControllerAction = "/" + DEFAULT_CONTROLLER + "/" + DEFAULT_ACTION;
        String defaultAction = "/" + DEFAULT_ACTION;

        if (path.length() > 0 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (path.endsWith(defaultControllerAction)) {
            path = path.substring(0, path.length() - defaultControllerAction.length());
        } else if (path.endsWith(defaultAction)) {
            path = path.substring(0, path.length() - defaultAction.length());
        }

        if (path.length() <= 0) {
            path = "/";
        }
        return path;
    }

    public String getPath() {
        return path;
    }

    public String getControllerPackage() {
        return controllerPackage;
    }

    public String getSubpackage() {
        return controllerPackage != null && packageRoot + 1 < controllerPackage.length() ?
                controllerPackage.substring(packageRoot + 1, controllerPackage.length()) : null;
    }

    public String getController() {
        int nextCursor = getNextCursor();
        String controller = cursor < nextCursor ? path.substring(cursor + 1, nextCursor) : null;
        return controller != null && controller.length() > 0 ? controller : DEFAULT_CONTROLLER;
    }

    public String getAction() {
        int nextCursor = getNextCursor();
        int nextNextCursor = getNextCursor(nextCursor);
        String action = nextCursor < nextNextCursor ? path.substring(nextCursor + 1, nextNextCursor) : null;
        return action != null && action.length() > 0 ? action : DEFAULT_ACTION;
    }

    public String getAfterController() {
        int nextCursor = getNextCursor();
        int lastCursor = getLastCursor();
        return nextCursor < lastCursor ? path.substring(nextCursor + 1, lastCursor) : null;
    }

    public String getAfterAction() {
        int nextCursor = getNextCursor();
        int nextNextCursor = getNextCursor(nextCursor);
        int lastCursor = getLastCursor();
        return nextNextCursor < lastCursor ? path.substring(nextNextCursor + 1, lastCursor) : null;
    }

    public String getBeforeController() {
        return cursor > 0 ? path.substring(0, cursor) : null;
    }

    public String getBeforeAction() {
        int nextCursor = getNextCursor();
        return nextCursor > 0 ? path.substring(0, nextCursor) : null;
    }

    public boolean isDefaultController() {
        return DEFAULT_CONTROLLER.equals(getController());
    }

    public boolean isDefaultAction() {
        return DEFAULT_ACTION.equals(getAction());
    }

    private int getNextCursor() {
        return getNextCursor(cursor);
    }

    private int getNextCursor(int previousCursor) {
        int nextCursor = path.indexOf("/", previousCursor + 1);
        return nextCursor >= 0 && nextCursor < getLastCursor() ? nextCursor : getLastCursor();
    }

    private int getLastCursor() {
        int lastCursor = path.indexOf("?");
        return lastCursor >= 0 ? lastCursor : path.length();
    }

    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = null;
        if (getLastCursor() < path.length()) {
            parameterMap = new HashMap<String, String[]>();
            String parametersString = path.substring(getLastCursor() + 1, path.length());

            String[] parametersSplit = parametersString.split("&");
            for (String parameter : parametersSplit) {
                String[] parameterSplit = parameter.split("=");
                if (parameterSplit.length == 2) {
                    String key = parameterSplit[0];
                    String value = parameterSplit[1];
                    if (parameterMap.get(key) == null) {
                        parameterMap.put(key, new String[]{value});
                    } else {
                        String[] oldValues = parameterMap.get(key);
                        String[] values = Arrays.copyOf(oldValues, oldValues.length + 1);
                        values[oldValues.length] = value;
                        parameterMap.put(key, values);
                    }
                }
            }
            parameterMap = Collections.unmodifiableMap(parameterMap);
        }

        return parameterMap == null ? Collections.<String, String[]>emptyMap() : parameterMap;
    }

    public Params getParams() {
        return new Params(getParameterMap());
    }

    public boolean isSamePackage(Route route) {
        return controllerPackage == null ? route.controllerPackage == null :
                controllerPackage.equals(route.controllerPackage);
    }

    public boolean isSameController(Route route) {
        return isSamePackage(route) && getController().equals(route.getController());
    }

    public boolean isSameAction(Route route) {
        return isSameController(route) && getAction().equals(route.getAction());
    }

    /**
     * Replaces the path with a new path. The new path can be absolute or
     * relative. If it is absolute (starts with a slash), the old path will
     * be replaced. If it is relative, it will be appended to the old path.
     *
     * @param path new path
     * @return a new route with the new path
     */
    public Route changePath(String path) {
        Route route = copy();

        if (path.equals(".")) {
            path = route.path.substring(0, getLastCursor());
        } else if (!path.startsWith("/")) {
            int lastSlash = route.path.substring(0, getLastCursor()).lastIndexOf("/");
            boolean endsWithController = route.path.length() > 1 && route.cursor == lastSlash;

            if (endsWithController) {
                path = route.path + "/" + path;
            } else {
                path = route.path.substring(0, lastSlash + 1) + path;
            }
        }
        route.setPath(path);
        return route;
    }

    /**
     * Shifts the current path position by one to the right. That means that what
     * was the action name is now the controller name and the next token is the
     * action.
     *
     * @return a new route with the shifted position
     */
    public Route shift() {
        Route route = copy();
        int nextCursor = route.getNextCursor();
        if (nextCursor >= 0) {
            route.cursor = nextCursor;
        }
        return route;
    }

    /**
     * Replaces the controller package.
     *
     * @param controllerPackage new controller package
     * @return a new route with the new controller package
     */
    public Route changeControllerPackage(String controllerPackage) {
        Route route = copy();
        route.controllerPackage = controllerPackage;
        return route;
    }

    /**
     * Adds a subpackage to the current package.
     *
     * @param subpackage subpackage to be added
     * @return a new route with the added subpackage
     */
    public Route addSubpackage(String subpackage) {
        Route route = copy();
        route.controllerPackage = route.controllerPackage != null ?
                route.controllerPackage + "." + subpackage : subpackage;
        return route;
    }

    private Route copy() {
        Route route = newRoute();
        route.controllerPackage = controllerPackage;
        route.packageRoot = packageRoot;
        route.path = path;
        route.cursor = cursor;
        return route;
    }

    public abstract String getUrl();
}
