package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class QuitController extends Controller implements IndexAction {

    @Override
    public void destroy() {
        Trail.append("a");
    }

    @Override
    public Result indexAction() {
        return success("a");
    }

    public Result fail(Params params) {
        throw new RuntimeException("fail");
    }
}
