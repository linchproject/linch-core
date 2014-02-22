package test.controllers.underscore1;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class _Controller extends Controller {

    public Result _(Params params) {
        return success("_-_");
    }
}
