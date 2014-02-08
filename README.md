# linch-core

## Example controller

    public class HelloWorldController extends Controller {

        Result index(Params params) {
            return render("Hello World");
        }
    }