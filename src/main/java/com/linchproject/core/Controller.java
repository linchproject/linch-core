package com.linchproject.core;

import com.linchproject.core.results.Ok;

import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Result render(String template) {
        return new Ok(Renderer.render(template, null));
    }

    protected Result render(String template, Map<String, Object> context) {
        return new Ok(Renderer.render(template, context));
    }
}
