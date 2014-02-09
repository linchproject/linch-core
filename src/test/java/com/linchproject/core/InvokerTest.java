package com.linchproject.core;

import com.linchproject.core.results.Error;
import com.linchproject.core.results.Ok;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Georg Schmidl
 */
public class InvokerTest {

    @Test
    public void testInvoke() throws Exception {
        Invoker invoker = new Invoker(getClass().getClassLoader(), "com.linchproject.core");

        Result result;

        result = invoker.invoke("my", "index", null);
        assertTrue(result instanceof Ok);
        assertEquals("index", ((Ok) result).getContent());

        result = invoker.invoke("invalid", "index", null);
        assertTrue(result instanceof Error);
        assertTrue(((Error) result).getException() instanceof ClassNotFoundException);

        result = invoker.invoke("my", "invalid", null);
        assertTrue(result instanceof Error);
        assertTrue(((Error) result).getException() instanceof NoSuchMethodException);
    }
}
