package com.linchproject.core;

import java.util.*;

/**
 * @author Georg Schmidl
 */
public abstract class Route {

    private static final String DEFAULT_CONTROLLER = "app";
    private static final String DEFAULT_ACTION = "index";

    private String controllerPackage;

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
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path.startsWith("/")) {
            this.path = path;
        } else if (path.equals(".")) {
            this.path = this.path.substring(0, getLastCursor());
        } else {
            this.path = this.path.substring(0, this.path.substring(0, getLastCursor()).lastIndexOf("/") + 1) + path;
        }
        this.cursor = 0;
    }

    public String getController() {
        int nextCursor = getNextCursor();
        String controller = cursor < nextCursor? path.substring(cursor + 1, nextCursor): null;
        return controller != null && controller.length() > 0? controller: DEFAULT_CONTROLLER;
    }

    public String getAction() {
        int nextCursor = getNextCursor();
        int nextNextCursor = getNextCursor(nextCursor);
        String action = nextCursor < nextNextCursor? path.substring(nextCursor + 1, nextNextCursor): null;
        return action != null && action.length() > 0? action: DEFAULT_ACTION;
    }

    public String getAfterController() {
        int nextCursor = getNextCursor();
        int lastCursor = getLastCursor();
        return nextCursor < lastCursor? path.substring(nextCursor + 1, lastCursor): null;
    }

    public String getAfterAction() {
        int nextCursor = getNextCursor();
        int nextNextCursor = getNextCursor(nextCursor);
        int lastCursor = getLastCursor();
        return nextNextCursor < lastCursor? path.substring(nextNextCursor + 1, lastCursor): null;
    }

    private int getNextCursor() {
        return getNextCursor(cursor);
    }

    private int getNextCursor(int previousCursor) {
        int nextCursor = path.indexOf("/", previousCursor + 1);
        return nextCursor >= 0 && nextCursor < getLastCursor()? nextCursor: getLastCursor();
    }

    private int getLastCursor() {
        int lastCursor = path.indexOf("?");
        return lastCursor >= 0? lastCursor: path.length();
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

        return parameterMap == null? Collections.<String, String[]>emptyMap(): parameterMap;
    }

    public boolean isSamePackage(Route route) {
        return controllerPackage == null? route.controllerPackage == null:
                controllerPackage.equals(route.controllerPackage);
    }

    public boolean isSameController(Route route) {
        return isSamePackage(route) && getController().equals(route.getController());
    }

    public boolean isSameAction(Route route) {
        return isSameController(route) && getAction().equals(route.getAction());
    }

    public Route copy() {
        Route route = newRoute();
        route.controllerPackage = controllerPackage;
        route.path = path;
        route.cursor = cursor;
        return route;
    }

    public Route shift(String subPackage) {
        Route route = copy();
        route.controllerPackage = route.controllerPackage != null? route.controllerPackage + "." + subPackage: subPackage;

        int nextCursor = route.getNextCursor();
        if (nextCursor >= 0) {
            route.cursor = nextCursor;
        }
        return route;
    }

    protected abstract Route newRoute();

    public abstract String getUrl();
}
