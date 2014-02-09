package com.linchproject.core.results;

import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class Ok implements Result {

    private String content;

    public Ok() {
    }

    public Ok(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
