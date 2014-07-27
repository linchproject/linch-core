package test.controllers.underscore1;

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
        return success("next-_");
    }

    @Override
    public Result indexAction() {
        return success("next-index");
    }
}
