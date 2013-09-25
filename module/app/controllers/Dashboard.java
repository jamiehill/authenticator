package controllers;

import core.SessionKeys;
import core.utils.CacheUtils;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.dashboard;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
@Security.Authenticated(Authenticated.class)
public class Dashboard extends Controller
{
    /**
     * Main (secured) application entry point
     * @return
     */
    public static Result index()
    {
        String username = session().get(SessionKeys.USERNAME);
        User user = User.findByUsername(username);

        CacheUtils.noCache(response());
        return ok(dashboard.render(user));
    }

}
