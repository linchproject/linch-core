package test.controllers.subpackage;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.core.Route;

/**
 * @author Georg Schmidl
 */
public class DispatcherController extends Controller {

    public Result _(Params params) {
        Route route = this.route.copy();
        route.shift();
        route.addSubPackage("subsubpackage");
        return dispatch(route);
    }
}
