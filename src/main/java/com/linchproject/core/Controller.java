package com.linchproject.core;

import com.linchproject.core.results.*;
import com.linchproject.core.results.Error;

import java.io.InputStream;

/**
 * @author Georg Schmidl
 */
public abstract class Controller {

    protected Route route;

    public void _init() {

    }

    public void _quit(Exception e) {

    }

    protected Result success(String content) {
        return new Success(content);
    }

    protected Result binary(InputStream inputStream) {
        return new Binary(inputStream);
    }

    protected Result redirect(Route route)  {
        return new Redirect(route);
    }

    protected Result dispatch(Route route)  {
        return new Dispatch(route);
    }

    protected Result error(String message)  {
        return new Error(message);
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
