package com.linchproject.core.renderer;

import com.linchproject.core.Renderer;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * @author Georg Schmidl
 */
public class MustacheRendererTest {
    @Test
    public void testRender() throws Exception {
        Renderer renderer = new MustacheRenderer();

        String result;

        result = renderer.render("name", null);
        assertEquals("", result);

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "John");

        result = renderer.render("name", context);
        assertEquals("John", result);
    }
}
