package controllers;

import core.SessionKeys;
import play.mvc.Http;
import play.mvc.Result;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */
public class Authenticated extends play.mvc.Security.Authenticator
{
    @Override
    public String getUsername(Http.Context ctx)
    {
        return ctx.session().get(SessionKeys.USERNAME);
    }

    @Override
    public Result onUnauthorized(Http.Context ctx)
    {
        return redirect(routes.Authenticate.index());
    }
}
