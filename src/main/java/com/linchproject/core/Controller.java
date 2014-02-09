package com.linchproject.core;

import com.linchproject.core.results.Ok;

import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Renderer renderer;

    protected Result ok() {
        return new Ok(null);
    }

    protected Result ok(String content) {
        return new Ok(content);
    }

    protected Result render(String template) {
        return ok(renderer.render(template, null));
    }

    protected Result render(String template, Map<String, Object> context) {
        return ok(renderer.render(template, context));
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
