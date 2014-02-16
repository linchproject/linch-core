package com.linchproject.core;

/**
 * @author Georg Schmidl
 */
public interface Instantiator {

    Object instantiate(Class<?> clazz);
}
