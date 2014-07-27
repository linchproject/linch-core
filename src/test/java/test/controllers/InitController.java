package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.Route;
import com.linchproject.core.actions.IndexAction;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class InitController extends Controller implements IndexAction {

    @Override
    public void init() {
        Trail.append("a");
    }

    @Override
    public Result indexAction() {
        return success("a");
    }

    public Result initAction() {
        Route route = this.route.copy();
        route.shift();
        route.addSubPackage("subpackage");
        return dispatch(route);
    }
}
