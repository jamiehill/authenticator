package core.providers;

import controllers.Authenticate;
import core.exceptions.AuthenticityException;
import core.SessionKeys;
import models.User;
import play.Application;
import play.Configuration;
import play.Plugin;
import play.libs.WS;
import play.mvc.Http;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class AuthenticityProvider extends Plugin
{

    //-----------------------------------------------------
    //
    // Startup/shutdown
    //
    //-----------------------------------------------------

    @Override
    public void onStart()
    {
        // check the identity of this provide has been set
        String identity = getIdentity();
        if (identity == null || identity.equals(""))
            throw new RuntimeException(
                    "No Identity set for current AuthenticityProvider :: "+this);

        // if 'requiredSettings' have been specified, we need to check
        // that each setting has been provided to us in the Configuration
        // node in secure.conf, for this AuthenticityProvider.
        final List<String> requiredSettings = requiredSettingKeys();
        if (requiredSettings != null)
        {
            Configuration config = getConfig();
            if (config == null)
                throw new RuntimeException(
                        "No AuthenticityProvider configuration specified in secure2.conf for "+getIdentity());

            // iterate through required settings to check
            // they've all been provided accordingly ...
            for (final String key : requiredSettings)
            {
                final String setting = config.getString(key);
                if (setting == null || "".equals(setting))
                    throw new RuntimeException("Provider '" + getIdentity()
                            + "' missing needed setting '" + key + "'");
            }
        }

        // register this provider in the Registry
        Authenticate.Registry.register(getIdentity(), this);
    }


    @Override
    public void onStop()
    {
        // unregister this provider
        Authenticate.Registry.unregister(getIdentity());
    }


    //-----------------------------------------------------
    //
    // Constructor.
    //
    //-----------------------------------------------------
    private final Application application;

    /**
     * @param application
     */
    public AuthenticityProvider(Application application)
    {
        this.application = application;
    }


    //-----------------------------------------------------
    //
    // Public - route mappings
    //
    //-----------------------------------------------------

    /**
     * Returns either a User object or null if not authenticated
     * @return
     * @throws AuthenticityException
     */
    public abstract User authenticate(final Authenticate.Login login,
                                      final Http.Context ctx) throws AuthenticityException;


    /**
     * Logs out and clears current session
     */
    public void logout(final Http.Context ctx)
    {
        ctx.session().remove(SessionKeys.USERNAME);
        ctx.session().remove(SessionKeys.EMAIL);
        ctx.session().remove(SessionKeys.CONSUMER_KEY);
        ctx.session().remove(SessionKeys.CONSUMER_SECRET);
        ctx.session().remove(SessionKeys.TOKEN);
        ctx.session().clear();
    }


    /**
     * Override for secure provider identity
     * @return
     */
    public abstract String getIdentity();


    //-----------------------------------------------------
    //
    // Private
    //
    //-----------------------------------------------------


    /**
     * Factory method to retrieve/generate a User instance
     * @param login
     * @param data
     * @return
     */
    protected User getUser(Authenticate.Login login, Object data)
    {
        User user = User.findByUsername(login.username);
        if (user == null)
        {
            user = new User();
            user.username = login.username;
            user.displayName = login.username.substring(0,1).toUpperCase() + login.username.substring(1);
            user.password = login.password;
            user.save();
        }
        return user;
    }


    /**
     * Helper method for returning this providers config
     * @return
     */
    protected Configuration getConfig()
    {
        return Authenticate.getProvidersConfig().getConfig(getIdentity());
    }


    /**
     * Helper method for returning this providers config
     * @return
     */
    protected String getSetting(String setting)
    {
        return getConfig().getString(setting);
    }

    /**
     * @param ctx
     * @param reponse
     * @return
     */
    protected abstract User doAuth(Authenticate.Login login,
                                   WS.Response reponse,
                                   Http.Context ctx)
            throws AuthenticityException;


    /**
     * Provides a hook for assessing whether response
     * payload should throw authenticity exception
     * @param response
     * @return
     * @throws AuthenticityException
     */
    protected Object parseResponse(WS.Response response)
    {
        if (response == null)
            return new AuthenticityException();
        return null;
    }


    /**
     * Required settings for this SecurePorovider
     */
    protected List<String> requiredSettingKeys()
    {
        return Arrays.asList();
    }


}

