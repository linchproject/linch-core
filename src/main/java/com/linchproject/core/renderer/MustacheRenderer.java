package com.linchproject.core.renderer;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.linchproject.core.Renderer;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class MustacheRenderer implements Renderer {

    public String render(String template, Map<String, Object> context) {
        StringWriter writer = new StringWriter();

        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(template + ".mustache");
        mustache.execute(writer, context);

        return writer.toString();
    }
}
