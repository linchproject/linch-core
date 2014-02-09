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

        route = new Route("a", null);
        assertEquals("a", route.getController());

        route = new Route();
        assertEquals("app", route.getController());
    }

    @Test
    public void testGetAction() throws Exception {
        Route route;

        route = new Route(null, "b");
        assertEquals("b", route.getAction());

        route = new Route();
        assertEquals("index", route.getAction());
    }

    @Test
    public void testGetParams() throws Exception {
        Params params = new Params(new HashMap<String, String[]>(){{
            put("a", new String[] {"b"});
        }});

        Route route;

        route = new Route(null, null, params);
        assertEquals(params, route.getParams());

        route = new Route();
        assertEquals(Params.getEmptyParams(), route.getParams());
    }

    @Test
    public void testGetDefaultRoute() throws Exception {

    }
}
