package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.core.actions._Action;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class Init2Controller extends Controller implements _Action, IndexAction {

    @Override
    public void init() {
        Trail.append("a");
    }

    @Override
    public Result _Action() {
        return dispatch(route);
    }

    @Override
    public Result indexAction() {
        return success("a");
    }
}
