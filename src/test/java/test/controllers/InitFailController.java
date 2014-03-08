package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class InitFailController extends Controller {
    @Override
    public void init() {
        throw new RuntimeException("fail");
    }

    public Result index(Params params) {
        return success("a");
    }
}
