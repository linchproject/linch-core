package test.controllers.underscore3;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;
import com.linchproject.core.actions._Action;

/**
 * @author Georg Schmidl
 */
public class NextController extends Controller implements _Action, IndexAction {

    @Override
    public Result _Action() {
        return dispatch(route);
    }

    @Override
    public Result indexAction() {
        return success("next-index");
    }
}
