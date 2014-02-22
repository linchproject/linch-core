package test.controllers.underscore2;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class NextController extends Controller {

    public Result _(Params params) {
        return success("next-_");
    }

    public Result index(Params params) {
        return success("next-index");
    }
}
