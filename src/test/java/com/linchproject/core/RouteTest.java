package com.linchproject.core;

import org.junit.Test;
import test.RouteImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
    public void testGetParameterMap() throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>(){{
            put("a", new String[] {"b"});
        }};

        Route route;

        route = new RouteImpl();
        route.setParameterMap(parameterMap);
        assertEquals(parameterMap, route.getParameterMap());

        route = new RouteImpl();
        assertEquals(new HashMap<String, String[]>(), route.getParameterMap());
    }

    @Test
    public void testCopy() throws Exception {
        Route route = new RouteImpl();
        route.setSubPackage("a");
        route.setController("b");
        route.setAction("c");
        route.setTail("d");
        route.setParameterMap(new HashMap<String, String[]>() {{
            put("1", new String[]{"1", "2"});
        }});

        Route newRoute = route.copy();
        assertEquals("a", newRoute.getSubPackage());
        assertEquals("b", newRoute.getController());
        assertEquals("c", newRoute.getAction());
        assertEquals("d", newRoute.getTail());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));
    }

    @Test
    public void testShift() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setController("b");
        route.setAction("c");
        route.setTail("d");
        route.setParameterMap(new HashMap<String, String[]>() {{
            put("1", new String[]{"1", "2"});
        }});

        route = route.shift("z");

        assertEquals("z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertNull(route.getTail());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));


        route = new RouteImpl();
        route.setController("b");
        route.setAction("c");
        route.setTail("d/");
        route.setParameterMap(new HashMap<String, String[]>() {{
            put("1", new String[]{"1", "2"});
        }});

        route = route.shift("z");

        assertEquals("z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertNull(route.getTail());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));


        route = new RouteImpl();
        route.setSubPackage("a");
        route.setController("b");
        route.setAction("c");
        route.setTail("d/e");
        route.setParameterMap(new HashMap<String, String[]>() {{
            put("1", new String[]{"1", "2"});
        }});

        route = route.shift("z");

        assertEquals("a.z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("e", route.getTail());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));

        route = new RouteImpl();
        route.setSubPackage("a");
        route.setController("b");
        route.setAction("c");
        route.setTail("d/e/f");
        route.setParameterMap(new HashMap<String, String[]>() {{
            put("1", new String[]{"1", "2"});
        }});

        route = route.shift("z");

        assertEquals("a.z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("e/f", route.getTail());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));

    }
}
