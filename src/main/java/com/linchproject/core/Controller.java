package com.linchproject.core;

import com.linchproject.core.results.Success;

import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Renderer renderer;

    protected Result success() {
        return new Success(null);
    }

    protected Result success(String content) {
        return new Success(content);
    }

    protected Result render(String template) {
        return success(renderer.render(template, null));
    }

    protected Result render(String template, Map<String, Object> context) {
        return success(renderer.render(template, context));
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }
}
