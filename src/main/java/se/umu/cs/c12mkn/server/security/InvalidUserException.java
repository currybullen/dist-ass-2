package se.umu.cs.c12mkn.server.security;

/**
 * Created by currybullen on 11/13/16.
 */
public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
