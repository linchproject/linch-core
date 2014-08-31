# linch-core [![Build Status](https://travis-ci.org/linchproject/linch-core.svg)](https://travis-ci.org/linchproject/linch-core)

## Example controller

    public class HelloWorldController extends Controller {

        public Result indexAction() {
            return success("Hello World");
        }
    }