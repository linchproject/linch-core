package com.linchproject.core;

import com.linchproject.core.results.Binary;
import com.linchproject.core.results.Dispatch;
import com.linchproject.core.results.Redirect;
import com.linchproject.core.results.Success;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Route route;

    protected Result success() {
        return success(null);
    }

    protected Result success(String content) {
        return new Success(content);
    }

    protected Result binary(InputStream inputStream) {
        return new Binary(inputStream);
    }

    public Result redirect(Route route)  {
        return new Redirect(route);
    }

    public Result dispatch(Route route)  {
        return new Dispatch(route);
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    protected Map<String, Object> createContext() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("route", route);
        return context;
    }

    public boolean isPermitted() {
        return true;
    }
}
