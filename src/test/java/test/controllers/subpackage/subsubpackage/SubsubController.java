package test.controllers.subpackage.subsubpackage;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class SubsubController extends Controller {

    public Result index(Params params) {
        return success("subsub");
    }
}
