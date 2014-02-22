package test.controllers.underscore3;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class NextController extends Controller {

    public Result _(Params params) {
        return dispatch(route);
    }

    public Result index(Params params) {
        return success("next-index");
    }
}
