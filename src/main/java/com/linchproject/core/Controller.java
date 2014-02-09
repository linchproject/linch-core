package com.linchproject.core;

import com.linchproject.core.results.Ok;

import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Renderer renderer;

    protected Result render(String template) {
        return new Ok(renderer.render(template, null));
    }

    protected Result render(String template, Map<String, Object> context) {
        return new Ok(renderer.render(template, context));
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
