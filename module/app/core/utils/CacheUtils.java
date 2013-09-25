package core.utils;

import play.mvc.Http;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:38
 * To change this template use File | Settings | File Templates.
 */
public class CacheUtils {

    public static void noCache(final Http.Response response)
    {
        // http://stackoverflow.com/questions/49547/making-sure-a-web-page-is-not-cached-across-all-browsers
        response.setHeader(Http.Response.CACHE_CONTROL, "no-cache, no-store, must-revalidate");  // HTTP 1.1
        response.setHeader(Http.Response.PRAGMA, "no-cache");  // HTTP 1.0.
        response.setHeader(Http.Response.EXPIRES, "0");  // Proxies.
    }
}
