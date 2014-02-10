package com.linchproject.core;

import com.linchproject.core.results.Success;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Georg Schmidl
 */
public class ControllerTest {

    private MyController myController = new MyController();

    public class MyController extends Controller {

        public Result hello(Params params) {
            return success("Hello");
        }

        public Result nothing(Params params) {
            return success();
        }

        public Result name(Params params) {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("name", "John");
            return render("name", context);
        }

        public Result noName(Params params) {
            return render("name");
        }
    }


    @Test
    public void testOk() throws Exception {
        Result result;

        result = this.myController.hello(null);
        assertTrue(result instanceof Success);
        assertEquals("Hello", ((Success) result).getContent());

        result = this.myController.nothing(null);
        assertTrue(result instanceof Success);
        assertNull(((Success) result).getContent());

    }

    @Test
    public void testRender() throws Exception {
        Renderer renderer = mock(Renderer.class);
        this.myController.setRenderer(renderer);

        Result result;

        result = this.myController.name(null);
        assertTrue(result instanceof Success);
        verify(renderer).render(eq("name"), argThat(new ContextContains("name", "John")), any(Route.class));

        result = this.myController.noName(null);
        assertTrue(result instanceof Success);
        verify(renderer).render(eq("name"), isNull(Map.class), any(Route.class));
    }
}
