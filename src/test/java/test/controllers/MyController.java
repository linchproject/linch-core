package test.controllers;

import com.linchproject.core.Controller;
import com.linchproject.core.Params;
import com.linchproject.core.Result;

/**
 * @author Georg Schmidl
 */
public class MyController extends Controller {

    public Result index(Params params) {
        return success("index");
    }
}
