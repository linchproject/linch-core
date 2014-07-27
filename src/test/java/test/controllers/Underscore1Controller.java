package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.Route;
import com.linchproject.core.actions._Action;

/**
 * @author Georg Schmidl
 */
public class Underscore1Controller extends Controller implements _Action {

    @Override
    public Result _Action() {
        Route route = this.route.copy();
        route.shift();
        route.addSubPackage("underscore1");
        return dispatch(route);
    }
}
