package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class QuitFailController extends Controller {
    @Override
    public void _quit(Exception e) {
        throw new RuntimeException("fail");
    }

    public Result index(Params params) {
        return success("a");
    }
}
