package test.controllers.subpackage;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class InitController extends Controller {

    @Override
    public void _init() {
        Trail.append("b");
    }

    public Result index(Params params) {
        return success("b");
    }
}
