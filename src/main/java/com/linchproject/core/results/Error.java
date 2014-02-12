package com.linchproject.core.results;

import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class Error implements Result {

    private String message;
    private Exception exception;

    public Error() {
    }

    public Error(String message) {
        this(message, null);
    }

    public Error(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
