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
        Invoker invoker = new Invoker(getClass().getClassLoader());

        Result result;
        Route route;

        route = new RouteImpl("/my/index", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("index", ((Success) result).getContent());

        route = new RouteImpl("/invalid/index", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);

        route = new RouteImpl("/my/invalid", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);


        route = new RouteImpl("/dispatcher/sub/index", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("sub", ((Success) result).getContent());


        route = new RouteImpl("/dispatcher/dispatcher/subsub/index", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("subsub", ((Success) result).getContent());


        route = new RouteImpl("/underscore1/next/index", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("_-_", ((Success) result).getContent());

        route = new RouteImpl("/underscore2/next/index", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("next-_", ((Success) result).getContent());

        route = new RouteImpl("/underscore3/next/index", "test.controllers");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("next-index", ((Success) result).getContent());

    }
}
