package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.core.Route;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class InitController extends Controller {

    @Override
    public void init() {
        Trail.append("a");
    }

    public Result index(Params params) {
        return success("a");
    }

    public Result init(Params params) {
        Route route = this.route.copy();
        route.shift();
        route.addSubPackage("subpackage");
        return dispatch(route);
    }
}
