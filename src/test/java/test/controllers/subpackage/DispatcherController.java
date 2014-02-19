package test.controllers.subpackage;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class DispatcherController extends Controller {

    public Result _(Params params) {
        return dispatch(route.shift("subsubpackage"));
    }
}
