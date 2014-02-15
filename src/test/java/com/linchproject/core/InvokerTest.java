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
        Route route;

        route = new RouteImpl();
        route.setPath("/my/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("index", ((Success) result).getContent());

        route = new RouteImpl();
        route.setPath("/invalid/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);

        route = new RouteImpl();
        route.setPath("/my/invalid");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);


        route = new RouteImpl();
        route.setPath("/dispatcher/sub/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("sub", ((Success) result).getContent());


        route = new RouteImpl();
        route.setPath("/dispatcher/dispatcher/subsub/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("subsub", ((Success) result).getContent());

    }
}
