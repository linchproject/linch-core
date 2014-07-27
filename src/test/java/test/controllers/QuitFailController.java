package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Result;
import com.linchproject.core.actions.IndexAction;

/**
 * @author Georg Schmidl
 */
public class QuitFailController extends Controller implements IndexAction {
    @Override
    public void destroy() {
        throw new RuntimeException("fail");
    }

    @Override
    public Result indexAction() {
        return success("a");
    }
}
