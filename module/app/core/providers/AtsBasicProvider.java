package core.providers;

import controllers.Authenticate;
import core.exceptions.AuthenticityException;
import core.SessionKeys;
import models.User;
import core.utils.Conf;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import play.Application;
import play.libs.F;
import play.libs.WS;
import play.libs.XPath;
import play.mvc.Http;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:29
 * To change this template use File | Settings | File Templates.
 */
public class AtsBasicProvider extends AuthenticityProvider
{
    final static String IDENTITY = "atsBasicProvider";
    final static String LOGIN_SERVICE_URI = "loginServiceUri";

    /**
     * @param app
     */
    public AtsBasicProvider(Application app)
    {
        super(app);
    }

    //-----------------------------------------------------
    //
    // Public
    //
    //-----------------------------------------------------

    @Override
    public User authenticate(final Authenticate.Login login,
                             final Http.Context ctx) throws AuthenticityException
    {
        String loginServiceUri = Conf.getRoot() + getSetting(LOGIN_SERVICE_URI);
        F.Promise<WS.Response> promise = WS.url(loginServiceUri)
                .setQueryParameter("username", login.username)
                .setQueryParameter("password", login.password)
                .setQueryParameter("requiredRole", "TRADER")
                .setQueryParameter("application", Conf.appName())
                .post("content");

        WS.Response response = promise.get();
        return doAuth(login, response, ctx);
    }


    @Override
    public String getIdentity()
    {
        return IDENTITY;
    }

    //-----------------------------------------------------
    //
    // Private
    //
    //-----------------------------------------------------

    /**
     * @param ctx
     * @param reponse
     * @return
     */
    @Override
    protected User doAuth(Authenticate.Login login,
                          WS.Response reponse,
                          Http.Context ctx) throws AuthenticityException
    {
        Object response = parseResponse(reponse);

        if (response instanceof SecurityException)
            throw (SecurityException)response;

        if (response instanceof Node)
        {
            String token = XPath.selectText("@sessionToken", response);
            User user = getUser(login, response);

            ctx.session().put(SessionKeys.USERNAME, user.username);
            ctx.session().put(SessionKeys.TOKEN, token);

            return user;
        }

        return null;
    }


    /**
     * Provides a hook for assessing whether response
     * payload should throw authenticity exception
     * @param response
     * @return
     * @throws SecurityException
     */
    @Override
    protected Object parseResponse(WS.Response response)
    {
        if (response == null)
            return new AuthenticityException();

        int httpStatus = response.getStatus();
        if (httpStatus != 200)
            return AuthenticityException.httpStatus(httpStatus);

        else
        {
            Document doc = ((Document)response.asXml());
            Node node = doc.getFirstChild();

            String status = XPath.selectText("@status", node);
            if (status.equals("OK")) return node;

            // Status not 'OK' so parse Error
            Node error = XPath.selectNode("Error", node);
            if (error != null)
            {
                String errorCode = XPath.selectText("@code", error);
                return AuthenticityException.errorCode(errorCode);
            }
        }

        return null;
    }


    @Override
    protected List<String> requiredSettingKeys()
    {
        final List<String> settings = new ArrayList<String>();
        settings.addAll(super.requiredSettingKeys());
        settings.add(LOGIN_SERVICE_URI);
        return settings;
    }
}
