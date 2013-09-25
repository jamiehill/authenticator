package core.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: Jamie
 * Date: 24/09/2013
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class AuthenticityException extends Exception
{
    public static final String UNSPECIFIED_EXCEPTION = "Something went wrong!";
    public static final String SERVICE_UNAVAILABLE_EXCEPTION = "Service unavailable";
    public static final String INVALID_CREDENTIALS_EXCEPTION = "Invalid credentials entered";
    public static final String PASSWORD_EXPIRED_EXCEPTION = "Your password has expired.";
    public static final String INVALID_ROLE_EXCEPTION = "Invalid role specified.";
    public static final String ALREADY_LOGGED_IN_EXCEPTION = "You're already logged in!";
    public static final String MAX_LOGINS_EXCEEDED_EXCEPTION = "Max logins with these credential!";

    /**
     * @param message
     */
    public AuthenticityException(final String message)
    {
        super(message);
    }

    /**
     *
     */
    public AuthenticityException()
    {
        super(UNSPECIFIED_EXCEPTION);
    }


    public static AuthenticityException httpStatus(int status)
    {
        return new AuthenticityException();
    }


    /**
     * Parses the provided errorCode to return a valid AuthenticityException
     * @param errorCode
     * @return
     */
    public static AuthenticityException errorCode(String errorCode)
    {
        String message = UNSPECIFIED_EXCEPTION;

        if (errorCode.equals("SERVICE_UNAVAILABLE"))
            message = SERVICE_UNAVAILABLE_EXCEPTION;

        else if (errorCode.equals("INVALID_CREDENTIALS"))
            message = INVALID_CREDENTIALS_EXCEPTION;

        else if (errorCode.equals("PASSWORD_EXPIRED"))
            message = PASSWORD_EXPIRED_EXCEPTION;

        else if (errorCode.equals("INVALID_ROLE"))
            message = INVALID_ROLE_EXCEPTION;

        else if (errorCode.equals("ALREADY_LOGGED_IN"))
            message = ALREADY_LOGGED_IN_EXCEPTION;

        else if (errorCode.equals("MAX_LOGINS_EXCEEDED"))
            message = MAX_LOGINS_EXCEEDED_EXCEPTION;

        return new AuthenticityException(message);
    }
}

