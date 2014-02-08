package com.linchproject.core.results;

import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class Redirect extends Result {

    private String target;

    public Redirect() {
    }

    public Redirect(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
