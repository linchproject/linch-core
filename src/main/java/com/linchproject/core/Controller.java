package com.linchproject.core;

import com.linchproject.core.results.Binary;
import com.linchproject.core.results.Dispatch;
import com.linchproject.core.results.Redirect;
import com.linchproject.core.results.Success;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Georg Schmidl
 */
public class Controller {

    protected Route route;
    protected Renderer renderer;
    protected UserAccessor userAccessor;

    protected Result success() {
        return success(null);
    }

    protected Result success(String content) {
        return new Success(content);
    }

    protected Result render(String template) {
        return render(template, Collections.<String, Object>emptyMap());
    }

    protected Result render(String template, Map<String, Object> actionContext) {
        Map<String, Object> context = createContext();
        context.putAll(actionContext);
        return success(renderer.render(template, context, route));
    }

    protected Result binary(InputStream inputStream) {
        return new Binary(inputStream);
    }

    public Result redirect(Route route)  {
        return new Redirect(route);
    }

    public Result dispatch(Route route)  {
        return new Dispatch(route);
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setRenderer(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setUserAccessor(UserAccessor userAccessor) {
        this.userAccessor = userAccessor;
    }

    protected Map<String, Object> createContext() {
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("route", route);
        return context;
    }

    protected User getUser() {
        User user = null;
        String userId = route.getUserId();
        if (userAccessor != null && userId != null) {
            user = userAccessor.getUser(userId);
        }
        return user;
    }

    public boolean isPermitted() {
        return true;
    }
}
