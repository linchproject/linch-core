package com.linchproject.core;

import com.linchproject.core.results.Error;
import com.linchproject.core.results.Success;
import org.junit.Test;
import test.RouteImpl;
import test.Trail;

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

        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/my/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("index", ((Success) result).getContent());

        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/invalid/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);

        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/my/invalid");
        result = invoker.invoke(route);
        assertTrue(result instanceof Error);


        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/dispatcher/sub/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("sub", ((Success) result).getContent());


        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/dispatcher/dispatcher/subsub/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("subsub", ((Success) result).getContent());


        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/underscore1/next/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("_-_", ((Success) result).getContent());

        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/underscore2/next/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("next-_", ((Success) result).getContent());

        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/underscore3/next/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("next-index", ((Success) result).getContent());

    }

    @Test
    public void testInit() throws Exception {
        Invoker invoker = new Invoker(getClass().getClassLoader());

        Result result;
        Route route;

        Trail.clear();
        route = new RouteImpl();
        route.setControllerPackage("test.controllers");
        route.setPath("/init/index");
        result = invoker.invoke(route);
        assertTrue(result instanceof Success);
        assertEquals("a", Trail.get());
    }
}
