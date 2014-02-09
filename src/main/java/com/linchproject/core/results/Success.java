package com.linchproject.core.results;

import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class Success implements Result {

    private String content;

    public Success() {
        this(null);
    }

    public Success(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
