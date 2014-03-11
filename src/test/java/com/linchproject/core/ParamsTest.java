package com.linchproject.core;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * @author Georg Schmidl
 */
public class ParamsTest {

     private Map<String, String[]> parameterMap;

    @Before
    public void setUp() throws Exception {
        this.parameterMap = new HashMap<String, String[]>();
        this.parameterMap.put("a", new String[]{"b"});
        this.parameterMap.put("aa", new String[]{"bb", "cc"});
    }

    @Test
    public void testGetValue() throws Exception {
        Params params = new Params(this.parameterMap);
        assertEquals("b", params.get("a"));
        assertEquals("bb", params.get("aa"));
        assertNull(params.get("invalid"));
    }

    @Test
    public void testGetValues() throws Exception {
        Params params = new Params(this.parameterMap);
        assertArrayEquals(new String[]{"b"}, params.getAll("a"));
        assertArrayEquals(new String[]{"bb", "cc"}, params.getAll("aa"));
        assertNull(params.getAll("invalid"));
    }

    @Test
    public void testGetMap() throws Exception {
        Params params = new Params(this.parameterMap);
        assertEquals(this.parameterMap, params.getMap());
    }
}
