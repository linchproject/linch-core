package com.linchproject.core;

import org.junit.Test;
import test.RouteImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Georg Schmidl
 */
public class RouteTest {

    @Test
    public void testGetController() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setPath("/a");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        route.setPath("/a/b");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        route.setPath("/a?1=2");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        route.setPath("/a/b?1=2");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        route.setPath("/a/b/c");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        route.setPath("/a/b/c?1=2");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        assertEquals("app", route.getController());
    }

    @Test
    public void testGetAction() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setPath("/a");
        assertEquals("index", route.getAction());

        route = new RouteImpl();
        route.setPath("/a/b");
        assertEquals("b", route.getAction());

        route = new RouteImpl();
        route.setPath("/a?1=2");
        assertEquals("index", route.getAction());

        route = new RouteImpl();
        route.setPath("/a/b?1=2");
        assertEquals("b", route.getAction());

        route = new RouteImpl();
        route.setPath("/a/b/c");
        assertEquals("b", route.getAction());

        route = new RouteImpl();
        route.setPath("/a/b/c?1=2");
        assertEquals("b", route.getAction());

        route = new RouteImpl();
        assertEquals("index", route.getAction());
    }

    @Test
    public void testGetAfterController() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setPath("/a");
        assertNull(route.getAfterController());

        route = new RouteImpl();
        route.setPath("/a/b");
        assertEquals("b", route.getAfterController());

        route = new RouteImpl();
        route.setPath("/a?1=2");
        assertNull(route.getAfterController());

        route = new RouteImpl();
        route.setPath("/a/b?1=2");
        assertEquals("b", route.getAfterController());

        route = new RouteImpl();
        route.setPath("/a/b/c");
        assertEquals("b/c", route.getAfterController());

        route = new RouteImpl();
        route.setPath("/a/b/c?1=2");
        assertEquals("b/c", route.getAfterController());

        route = new RouteImpl();
        assertEquals(null, route.getAfterController());
    }

    @Test
    public void testGetAfterAction() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setPath("/a");
        assertNull(route.getAfterAction());

        route = new RouteImpl();
        route.setPath("/a/b");
        assertNull(route.getAfterAction());

        route = new RouteImpl();
        route.setPath("/a?1=2");
        assertNull(route.getAfterAction());

        route = new RouteImpl();
        route.setPath("/a/b?1=2");
        assertNull(route.getAfterAction());

        route = new RouteImpl();
        route.setPath("/a/b/c");
        assertEquals("c", route.getAfterAction());

        route = new RouteImpl();
        route.setPath("/a/b/c?1=2");
        assertEquals("c", route.getAfterAction());

        route = new RouteImpl();
        assertEquals(null, route.getAfterAction());
    }

    @Test
    public void testGetParameterMap() throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>(){{
            put("a", new String[] {"b"});
        }};

        Route route;

        route = new RouteImpl();
        route.setPath("/a/b?a=b");
        assertTrue(parameterMap.size() == 1);
        assertArrayEquals(new String[]{"b"}, route.getParameterMap().get("a"));

        route = new RouteImpl();
        assertEquals(new HashMap<String, String[]>(), route.getParameterMap());
    }

    @Test
    public void testCopy() throws Exception {
        Route route = new RouteImpl();
        route.setSubPackage("a");
        route.setPath("/b/c/d?1=1&1=2");

        Route newRoute = route.copy();
        assertEquals("a", newRoute.getSubPackage());
        assertEquals("b", newRoute.getController());
        assertEquals("c", newRoute.getAction());
        assertEquals("d", newRoute.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));
    }

    @Test
    public void testShift() throws Exception {
        Route route;

        route = new RouteImpl();
        route.setPath("/b/c/d?1=1&1=2");

        route = route.shift("z");

        assertEquals("z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertNull(route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));


        route = new RouteImpl();
        route.setPath("/b/c/d/?1=1&1=2");

        route = route.shift("z");

        assertEquals("z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("", route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));


        route = new RouteImpl();
        route.setSubPackage("a");
        route.setPath("/b/c/d/e?1=1&1=2");

        route = route.shift("z");

        assertEquals("a.z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("e", route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));

        route = new RouteImpl();
        route.setSubPackage("a");
        route.setPath("/b/c/d/e/f?1=1&1=2");

        route = route.shift("z");

        assertEquals("a.z", route.getSubPackage());
        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("e/f", route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));

    }
}
