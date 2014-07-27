package test;

import com.linchproject.core.Route;

/**
 * @author Georg Schmidl
 */
public class RouteImpl extends Route {

    public RouteImpl() {
        super();
    }

    public RouteImpl(String path) {
        setPath(path);
    }

    public RouteImpl(String path, String controllerPackage) {
        setPath(path);
        setControllerPackage(controllerPackage);
    }

    @Override
    public String getUrl() {
        return getPath();
    }

    @Override
    public Route newRoute() {
        return new RouteImpl();
    }
}
