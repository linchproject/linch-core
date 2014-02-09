package com.linchproject.core;

/**
 * @author Georg Schmidl
 */
public class MyController extends Controller {

    public Result index(Params params) {
        return ok("index");
    }
}
