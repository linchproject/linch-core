package com.linchproject.core;

import com.linchproject.core.results.Binary;
import com.linchproject.core.results.Dispatch;
import com.linchproject.core.results.Redirect;
import com.linchproject.core.results.Success;

import java.io.InputStream;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Route route;
    protected Renderer renderer;

    protected Result success() {
        return new Success(null);
    }

    protected Result success(String content) {
        return new Success(content);
    }

    protected Result render(String template) {
        return success(renderer.render(template, null, route));
    }

    protected Result render(String template, Map<String, Object> context) {
        return success(renderer.render(template, context, route));
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

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
