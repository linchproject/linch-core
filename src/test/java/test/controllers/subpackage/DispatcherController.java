package test.controllers.subpackage;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions._Action;

/**
 * @author Georg Schmidl
 */
public class DispatcherController extends Controller implements _Action {

    @Override
    public Result _Action() {
        return dispatch(route.shift().addSubpackage("subsubpackage"));
    }
}
