package com.linchproject.core;

import com.linchproject.core.results.*;
import com.linchproject.core.results.Error;

import java.io.InputStream;

/**
 * @author Georg Schmidl
 */
public abstract class Controller {

    protected Route route;

    protected Result success(String content) {
        return new Success(content);
    }

    protected Result binary(String fileName, InputStream inputStream) {
        return new Binary(fileName, inputStream);
    }

    protected Result redirect(Route route)  {
        return new Redirect(route);
    }

    protected Result redirect(String path)  {
        return new Redirect(route.changePath(path));
    }

    protected Result dispatch()  {
        return new Dispatch(this.route);
    }

    protected Result dispatch(Route route)  {
        return new Dispatch(route);
    }

    protected Result dispatch(String path)  {
        return new Dispatch(route.changePath(path));
    }

    protected Result error(String message)  {
        return new Error(message);
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
