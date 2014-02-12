package com.linchproject.core.results;

import com.linchproject.core.Result;
import com.linchproject.core.Route;

/**
 * @author Georg Schmidl
 */
public class Dispatch implements Result {

    private Route route;

    public Dispatch() {
    }

    public Dispatch(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
