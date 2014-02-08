package com.linchproject.core;

import com.linchproject.core.results.Ok;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        MyController myController = new MyController();
        Result result = myController.index(null);

        assertTrue(result instanceof Ok);
        assertEquals("John", ((Ok) result).getContent());
    }
}
