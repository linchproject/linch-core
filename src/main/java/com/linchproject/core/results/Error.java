package com.linchproject.core.results;

import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class Error implements Result {

    private Exception exception;

    public Error() {
    }

    public Error(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
