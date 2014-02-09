package com.linchproject.core;

import com.linchproject.core.results.Ok;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author Georg Schmidl
 */
public class ControllerTest {

    public class MyController extends Controller {

        public Result index(Params params) {
            Map<String, Object> context = new HashMap<String, Object>();
            context.put("name", "John");

            return render("name", context);
        }
    }


    @Test
    public void testRender() throws Exception {
        Renderer renderer = mock(Renderer.class);

        MyController myController = new MyController();
        myController.setRenderer(renderer);
        Result result = myController.index(null);

        assertTrue(result instanceof Ok);
        verify(renderer).render(eq("name"), argThat(new ContextContains("name", "John")));
    }
}
