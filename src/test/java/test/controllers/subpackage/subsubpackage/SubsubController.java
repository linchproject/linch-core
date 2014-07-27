package test.controllers.subpackage.subsubpackage;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;

/**
 * @author Georg Schmidl
 */
public class SubsubController extends Controller implements IndexAction {

    @Override
    public Result indexAction() {
        return success("subsub");
    }
}
