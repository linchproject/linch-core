package com.linchproject.core.results;

import com.linchproject.core.Result;

import java.io.InputStream;

/**
 * @author Georg Schmidl
 */
public class Binary implements Result {

    InputStream inputStream;

    public Binary() {
    }

    public Binary(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
