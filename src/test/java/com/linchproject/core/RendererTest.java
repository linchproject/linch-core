package com.linchproject.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Georg Schmidl
 */
public class RendererTest {
    @Test
    public void testRender() throws Exception {
        String result;

        result = Renderer.render("name", null);
        assertEquals("", result);

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "John");

        result = Renderer.render("name", context);
        assertEquals("John", result);
    }
}
