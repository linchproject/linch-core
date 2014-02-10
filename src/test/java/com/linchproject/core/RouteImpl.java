package com.linchproject.core;

/**
 * @author Georg Schmidl
 */
public class RouteImpl extends Route {

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public Route newRoute() {
        return new RouteImpl();
    }
}
