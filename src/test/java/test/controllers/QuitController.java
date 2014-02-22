package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;
import test.Trail;

/**
 * @author Georg Schmidl
 */
public class QuitController extends Controller {

    @Override
    public void _quit(Exception e) {
        if (e == null) {
            Trail.append("a");
        } else {
            Trail.append("exception");
        }

    }

    public Result index(Params params) {
        return success("a");
    }

    public Result fail(Params params) {
        throw new RuntimeException("fail");
    }
}
