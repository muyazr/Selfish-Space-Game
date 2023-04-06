package selfish;
/*
@author Muyaz Rahman
@version 19.0.2
 */
public class GameException extends Exception {

    /**
     * Constructs a new `GameException` instance with the specified error message and cause.
     * @param msg the error message
     * @param e the cause of the error
     */
    public GameException(String msg, Throwable e) {
        super(msg, e);
        System.err.println(msg);
        e.printStackTrace(System.err);
    }

}