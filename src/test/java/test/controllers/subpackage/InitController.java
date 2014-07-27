package test.controllers.subpackage;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class InitController extends Controller implements IndexAction {

    @Override
    public void init() {
        Trail.append("b");
    }

    @Override
    public Result indexAction() {
        return success("b");
    }
}
