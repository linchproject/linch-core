package com.linchproject.core;

import com.linchproject.core.results.Error;
import com.linchproject.core.results.Success;
import org.junit.Test;
import test.RouteImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Georg Schmidl
 */
public class InvokerTest {

    @Test
    public void testInvoke() throws Exception {
        Invoker invoker = new Invoker(getClass().getClassLoader(), "test.controller", new Container());

        Result result;

        Route route = new RouteImpl();
        route.setController("my");
        route.setAction("index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("index", ((Success) result).getContent());

        route = new RouteImpl();
        route.setController("invalid");
        route.setAction("index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);

        route = new RouteImpl();
        route.setController("my");
        route.setAction("invalid");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);
    }
}
