package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class Init2Controller extends Controller {

    @Override
    public void init() {
        Trail.append("a");
    }

    public Result _(Params params) {
        return dispatch(route);
    }

    public Result index(Params params) {
        return success("a");
    }

}
