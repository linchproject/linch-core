package com.linchproject.core;

import com.linchproject.core.results.Success;
import org.junit.Test;

import static org.junit.Assert.*;

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
}
