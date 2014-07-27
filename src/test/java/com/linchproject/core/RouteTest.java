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
    public void testSetPath() throws Exception {
        Route route = new RouteImpl();
        route = route.changePath("/a/b/c?1=2");
        assertEquals("/a/b/c?1=2", route.getPath());
        route = route.changePath("d/e");
        assertEquals("/a/b/d/e", route.getPath());

        route = route.changePath("/a/b?next=/");
        route = route.changePath("c");
        assertEquals("/a/c", route.getPath());

        route = route.changePath("/a/index");
        assertEquals("/a", route.getPath());

        route = route.changePath("/app/index");
        assertEquals("/", route.getPath());

        route = route.changePath("/index/index");
        assertEquals("/index", route.getPath());

        route = route.changePath("/app/app");
        assertEquals("/app/app", route.getPath());

        route = route.changePath("/a");
        route = route.changePath("c");
        assertEquals("/a/c", route.getPath());
    }

    @Test
    public void testGetController() throws Exception {
        Route route;

        route = new RouteImpl("/a");
        assertEquals("a", route.getController());

        route = new RouteImpl("/a/b");
        assertEquals("a", route.getController());

        route = new RouteImpl("/a?1=2");
        assertEquals("a", route.getController());

        route = new RouteImpl("/a/b?1=2");
        assertEquals("a", route.getController());

        route = new RouteImpl("/a/b/c");
        assertEquals("a", route.getController());

        route = new RouteImpl("/a/b/c?1=2");
        assertEquals("a", route.getController());

        route = new RouteImpl();
        assertEquals("app", route.getController());
    }

    @Test
    public void testGetAction() throws Exception {
        Route route;

        route = new RouteImpl("/a");
        assertEquals("index", route.getAction());

        route = new RouteImpl("/a/b");
        assertEquals("b", route.getAction());

        route = new RouteImpl("/a?1=2");
        assertEquals("index", route.getAction());

        route = new RouteImpl("/a/b?1=2");
        assertEquals("b", route.getAction());

        route = new RouteImpl("/a/b/c");
        assertEquals("b", route.getAction());

        route = new RouteImpl("/a/b/c?1=2");
        assertEquals("b", route.getAction());

        route = new RouteImpl();
        assertEquals("index", route.getAction());
    }

    @Test
    public void testGetAfterController() throws Exception {
        Route route;

        route = new RouteImpl("/a");
        assertNull(route.getAfterController());

        route = new RouteImpl("/a/b");
        assertEquals("b", route.getAfterController());

        route = new RouteImpl("/a?1=2");
        assertNull(route.getAfterController());

        route = new RouteImpl("/a/b?1=2");
        assertEquals("b", route.getAfterController());

        route = new RouteImpl("/a/b/c");
        assertEquals("b/c", route.getAfterController());

        route = new RouteImpl("/a/b/c?1=2");
        assertEquals("b/c", route.getAfterController());

        route = new RouteImpl();
        assertEquals(null, route.getAfterController());
    }

    @Test
    public void testGetAfterAction() throws Exception {
        Route route;

        route = new RouteImpl("/a");
        assertNull(route.getAfterAction());

        route = new RouteImpl("/a/b");
        assertNull(route.getAfterAction());

        route = new RouteImpl("/a?1=2");
        assertNull(route.getAfterAction());

        route = new RouteImpl("/a/b?1=2");
        assertNull(route.getAfterAction());

        route = new RouteImpl("/a/b/c");
        assertEquals("c", route.getAfterAction());

        route = new RouteImpl("/a/b/c?1=2");
        assertEquals("c", route.getAfterAction());

        route = new RouteImpl();
        assertEquals(null, route.getAfterAction());
    }

    @Test
    public void testGetBeforeController() throws Exception {
        Route route;

        route = new RouteImpl("/");
        assertNull(route.getBeforeController());

        route = new RouteImpl("/a");
        assertNull(route.getBeforeController());

        route = new RouteImpl("/a/b");
        assertNull("b", route.getBeforeController());

        route = new RouteImpl("/a/b");
        route = route.shift();
        assertEquals("/a", route.getBeforeController());

    }

    @Test
    public void testGetBeforeAction() throws Exception {
        Route route;

        route = new RouteImpl("/");
        assertEquals("/", route.getBeforeAction());

        route = new RouteImpl("/a");
        assertEquals("/a", route.getBeforeAction());

        route = new RouteImpl("/a/b");
        assertEquals("/a", route.getBeforeAction());

        route = new RouteImpl("/a/b");
        route = route.shift();
        assertEquals("/a/b", route.getBeforeAction());

        route = new RouteImpl("/a/b/c");
        route = route.shift();
        assertEquals("/a/b", route.getBeforeAction());
    }

    @Test
    public void testGetParameterMap() throws Exception {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>(){{
            put("a", new String[] {"b"});
        }};

        Route route;

        route = new RouteImpl("/a/b?a=b");
        assertTrue(parameterMap.size() == 1);
        assertArrayEquals(new String[]{"b"}, route.getParameterMap().get("a"));

        route = new RouteImpl();
        assertEquals(new HashMap<String, String[]>(), route.getParameterMap());
    }

    @Test
    public void testIsSamePackage() throws Exception {
        Route route1;
        Route route2;

        route1 = new RouteImpl();
        route2 = new RouteImpl();
        assertTrue(route1.isSamePackage(route2));

        route1 = new RouteImpl("/", "a");
        route2 = new RouteImpl("/","a");
        assertTrue(route1.isSamePackage(route2));

        route1 = new RouteImpl("/", "a");
        route2 = new RouteImpl();
        assertFalse(route1.isSamePackage(route2));
    }

    @Test
    public void testIsSameController() throws Exception {
        Route route1;
        Route route2;

        route1 = new RouteImpl();
        route2 = new RouteImpl();
        assertTrue(route1.isSameController(route2));

        route1 = new RouteImpl("/a");
        route2 = new RouteImpl("/a");
        assertTrue(route1.isSameController(route2));

        route1 = new RouteImpl("/a");
        route2 = new RouteImpl();
        assertFalse(route1.isSameController(route2));
    }

    @Test
    public void testIsSameAction() throws Exception {
        Route route1;
        Route route2;

        route1 = new RouteImpl();
        route2 = new RouteImpl();
        assertTrue(route1.isSameAction(route2));

        route1 = new RouteImpl("/a/b");
        route2 = new RouteImpl("/a/b");
        assertTrue(route1.isSameAction(route2));

        route1 = new RouteImpl("/a/b");
        route2 = new RouteImpl();
        assertFalse(route1.isSameAction(route2));
    }

    @Test
    public void testShift() throws Exception {
        Route route;

        route = new RouteImpl("/b/c/d?1=1&1=2");

        route = route.shift();

        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertNull(route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));


        route = new RouteImpl("/b/c/d/?1=1&1=2");

        route = route.shift();

        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("", route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));


        route = new RouteImpl("/b/c/d/e?1=1&1=2");

        route = route.shift();

        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("e", route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));

        route = new RouteImpl("/b/c/d/e/f?1=1&1=2");

        route = route.shift();

        assertEquals("c", route.getController());
        assertEquals("d", route.getAction());
        assertEquals("e/f", route.getAfterAction());
        assertTrue(route.getParameterMap().size() == 1);
        assertTrue(Arrays.equals(new String[]{"1", "2"}, route.getParameterMap().get("1")));

    }

    @Test
    public void testAddSubPackage() throws Exception {
        Route route;

        route = new RouteImpl();
        assertNull(route.getControllerPackage());
        assertNull(route.getSubpackage());

        route = new RouteImpl("/", "a");
        assertEquals("a", route.getControllerPackage());
        assertNull(route.getSubpackage());


        route = new RouteImpl();
        route = route.addSubpackage("a");
        assertEquals("a", route.getControllerPackage());
        assertEquals("a", route.getSubpackage());

        route = new RouteImpl("/", "a");
        route = route.addSubpackage("b");
        assertEquals("a.b", route.getControllerPackage());
        assertEquals("b", route.getSubpackage());

        route = new RouteImpl("/", "a");
        route = route.addSubpackage("b");
        route = route.addSubpackage("c");
        assertEquals("a.b.c", route.getControllerPackage());
        assertEquals("b.c", route.getSubpackage());

        route = new RouteImpl("/", "b");
        route = route.addSubpackage("c");
        assertEquals("b.c", route.getControllerPackage());
        assertEquals("c", route.getSubpackage());
    }
}
