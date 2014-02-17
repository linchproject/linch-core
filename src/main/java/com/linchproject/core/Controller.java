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

    public void init() {

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

    public boolean isPermitted() {
        return true;
    }

    public void onError() {

    }

    public void onSuccess() {

    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
