package com.linchproject.core.results;

import com.linchproject.core.Result;
import com.linchproject.core.Route;

/**
 * @author Georg Schmidl
 */
public class Redirect implements Result {

    private Route route;

    public Redirect() {
        this(null);
    }

    public Redirect(Route route) {
        this.route = route;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
