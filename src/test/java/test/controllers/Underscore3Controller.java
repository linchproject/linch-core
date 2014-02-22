package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class Underscore3Controller extends Controller {
    public Result _(Params params) {
        return dispatch(route.shift("underscore3"));
    }
}
