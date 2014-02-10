package com.linchproject.core;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

/**
 * @author Georg Schmidl
 */
public class RouteTest {

    @Test
    public void testGetController() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setController("a");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        assertEquals("app", route.getController());
    }

    @Test
    public void testGetAction() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setAction("b");
        assertEquals("b", route.getAction());

        route = new RouteImpl();
        assertEquals("index", route.getAction());
    }

    @Test
    public void testGetParams() throws Exception {
        Params params = new Params(new HashMap<String, String[]>(){{
            put("a", new String[] {"b"});
        }});

        Route route;

        route = new RouteImpl();
        route.setParams(params);
        assertEquals(params, route.getParams());

        route = new RouteImpl();
        assertEquals(Params.getEmptyParams(), route.getParams());
    }

    @Test
    public void testGetDefaultRoute() throws Exception {

    }
}
