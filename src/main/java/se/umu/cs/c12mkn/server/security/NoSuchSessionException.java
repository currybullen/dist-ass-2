package se.umu.cs.c12mkn.server.security;

/**
 * Created by currybullen on 11/13/16.
 */
public class NoSuchSessionException extends Exception {
    public NoSuchSessionException(String message) {
        super(message);
    }
}
