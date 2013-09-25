package core.utils;

import play.Play;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:42
 * To change this template use File | Settings | File Templates.
 */
public class Conf
{
    /**
     * Retrieves a key from the application.conf. Throws exception if key not found.
     * @param key
     * @return
     */
    public static String get(String key) throws Exception
    {
        String value = null;
        try {
            value = Play.application().configuration().getString(key);
        }
        catch (Exception e) {
            throw new Exception("Couldn't find config for " + key);
        }
        return null;
    }


    /**
     * Retrieves a key from the application.conf. Returns empty string if not found
     * @param key
     * @return
     */
    public static String getOrEmpty(String key)
    {
        String value = Play.application().configuration().getString(key);

        if (value == null || value.equals(""))
            value = "";

        return value;
    }


    /**
     * Retrieves the name of the application
     * @return
     */
    public static String appName()
    {
        return getOrEmpty("app.name");
    }


    /**
     * Retrieves the name of the application
     * @return
     */
    public static String getRoot()
    {
        return getOrEmpty("root.url");
    }


    /**
     * Retrieves the name of the application
     * @return
     */
    public static String refDataService()
    {
        return getRoot() +"RefDataService";
    }
}
