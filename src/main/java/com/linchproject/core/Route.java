package com.linchproject.core;

import java.util.*;

/**
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

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
        this.packageRoot = this.controllerPackage != null ? controllerPackage.length() : -1;
    }

    public String getPath() {
        return path;
    }

    /**
     * Replaces the path with a new path. The new path can be absolute or
     * relative. If it is absolute (starts with a slash), the old path will
     * be replaced. If it is relative, it will be appended to the old path.
     *
     * @param path new path
     */
    public void setPath(String path) {
        if (path.startsWith("/")) {
            this.path = path;
        } else if (path.equals(".")) {
            this.path = this.path.substring(0, getLastCursor());
        } else {
            int lastSlash = this.path.substring(0, getLastCursor()).lastIndexOf("/");
            boolean endsWithController = this.path.length() > 1 && this.cursor == lastSlash;

            if (endsWithController) {
                this.path += "/" + path;
            } else {
                this.path = this.path.substring(0, lastSlash + 1) + path;
            }
        }

        this.path = normalize(this.path);
        this.cursor = 0;
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

    /**
     * Removes trailing slashes and trialing default actions or controllers
     *
     * @param path path to be normalized
     * @return the normalized path
     */
    private static String normalize(String path) {
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

    public void shift() {
        int nextCursor = getNextCursor();
        if (nextCursor >= 0) {
            cursor = nextCursor;
        }
    }

    public void addSubPackage(String subPackage) {
        controllerPackage = controllerPackage != null ? controllerPackage + "." + subPackage : subPackage;
    }

    public String getSubPackage() {
        return controllerPackage != null && packageRoot + 1 < controllerPackage.length() ?
                controllerPackage.substring(packageRoot + 1, controllerPackage.length()) : null;
    }

    public Route copy() {
        Route route = newRoute();
        route.controllerPackage = controllerPackage;
        route.packageRoot = packageRoot;
        route.path = path;
        route.cursor = cursor;
        return route;
    }

    protected abstract Route newRoute();

    public abstract String getUrl();
}
