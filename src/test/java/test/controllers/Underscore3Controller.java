package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions._Action;

/**
 * @author Georg Schmidl
 */
public class Underscore3Controller extends Controller implements _Action {

    @Override
    public Result _Action() {
        return dispatch(route.shift().addSubpackage("underscore3"));
    }
}
