package test;

import com.linchproject.core.Route;

/**
 * @author Georg Schmidl
 */
public class RouteImpl extends Route {

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public void setUserId(String userId) {

    }

    @Override
    public Route newRoute() {
        return new RouteImpl();
    }
}
