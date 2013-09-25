package controllers;

import core.SessionKeys;
import core.exceptions.AuthenticityException;
import core.providers.AuthenticityProvider;
import core.utils.CacheUtils;
import models.User;
import play.Configuration;
import play.Logger;
import play.Play;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static play.data.Form.form;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:24
 * To change this template use File | Settings | File Templates.
 */
public class Authenticate extends Controller
{
    private static final String SETTING_KEY_SECURE2 = "authenticator";
    private static final String SETTING_KEY_PROVIDERS = "providers";

    //-----------------------------------------------------
    //
    // Routes
    //
    //-----------------------------------------------------

    /**
     * Display the login page or dashboard if authenticated
     *
     * @return partials page or dashboard
     */
    public static Result index()
    {
        String username = session().get(
                SessionKeys.USERNAME);

        if (username != null)
        {
            User user = User.findByUsername(username);
            if (user != null)
                return redirect(routes.Dashboard.index());
            else session().clear();
        }

        return ok(login.render(form(Login.class)));
    }

    /**
     * Handle form submission.
     *
     * @return Dashboard if auth OK or partials form if auth KO
     */
    public static Result login() throws IOException
    {
        Form<Login> loginForm = form(Login.class)
                .bindFromRequest();

        if (loginForm.hasErrors())
            return badRequest(login.render(loginForm));

        return redirect(routes.Dashboard.index());
    }


    /**
     * Logout and clean the session.
     *
     * @return Index page
     */
    public static Result logout()
    {
        Registry.getProvider().logout(ctx());
        flash("success", Messages.get("error.logged.out"));

        CacheUtils.noCache(response());
        return redirect(routes.Authenticate.index());
    }

    //-----------------------------------------------------
    //
    // Statics
    //
    //-----------------------------------------------------

    /**
     * Login class used by Login Form.
     */
    public static class Login
    {
        @Constraints.Required
        public String username;

        @Constraints.Required
        public String password;

        /**
         * Validate the authentication.
         *
         * @return null if validation ok, string with details otherwise
         */
        public String validate() {

            if (username == null ||
                    password == null)
                return Messages.get("error.missing.credentials");

            User user = null;
            try {
                user = Registry.getProvider()
                        .authenticate(this, ctx());
            } catch (AuthenticityException e) {
                return e.getMessage();
            }

            if (user == null)
                return Messages.get("error.invalid.credentials");

            return null;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }


    /**
     * Static Registry implementation
     */
    public abstract static class Registry
    {
        private static AuthenticityProvider defaultProvider;
        private static Map<String, AuthenticityProvider> providers = new HashMap<String, AuthenticityProvider>();

        /**
         * Registers a new AuthenticityProvider
         * @param provider
         */
        public static void register(final String identity, final AuthenticityProvider provider)
        {
            Logger.info("AuthenticityProvider registered " + identity);

            defaultProvider = provider;
            providers.put(identity, provider);
        }


        /**
         * Unregisters a AuthenticityProvider
         * @param provider
         */
        public static void unregister(final String provider)
        {
            Logger.info("AuthenticityProvider unregistered "+provider);
            providers.remove(provider);
        }


        /**
         * @param provider
         * @return
         */
        public static AuthenticityProvider get(final String provider)
        {
            return providers.get(provider);
        }


        /**
         * @return
         */
        public static AuthenticityProvider getProvider()
        {
            // either return default provider
            if (defaultProvider != null)
                return get(defaultProvider.getIdentity());

            // otherwise just get the first in the registry
            if (providers.size() > 0)
                for (Map.Entry<String, AuthenticityProvider> entry : providers.entrySet())
                    return entry.getValue();

            return null;
        }


        /**
         * @return
         */
        public static Collection<AuthenticityProvider> getProviders()
        {
            return providers.values();
        }


        /**
         * @param provider
         * @return
         */
        public static boolean hasProvider(final String provider)
        {
            return providers.containsKey(provider);
        }
    }


    public static Configuration getConf()
    {
        return Play.application().configuration()
                .getConfig(SETTING_KEY_SECURE2);
    }


    public static Configuration getProvidersConfig()
    {
        return getConf().getConfig(SETTING_KEY_PROVIDERS);
    }
}
