package se.umu.cs.c12mkn.server.security.exception;

/**
 * Created by currybullen on 11/17/16.
 */
public class NoSuchUsernameException extends Throwable {
    public NoSuchUsernameException(String message) {
        super(message);
    }
}
