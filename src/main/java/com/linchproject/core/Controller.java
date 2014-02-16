package com.linchproject.core;

import com.linchproject.core.results.Binary;
import com.linchproject.core.results.Dispatch;
import com.linchproject.core.results.Redirect;
import com.linchproject.core.results.Success;

import java.io.InputStream;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Route route;

    protected Result success(String content) {
        return exit(new Success(content));
    }

    protected Result binary(InputStream inputStream) {
        return exit(new Binary(inputStream));
    }

    protected Result redirect(Route route)  {
        return exit(new Redirect(route));
    }

    protected Result dispatch(Route route)  {
        return exit(new Dispatch(route));
    }

    protected Result exit(Result result) {
        return result;
    }

    public boolean isPermitted() {
        return true;
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
