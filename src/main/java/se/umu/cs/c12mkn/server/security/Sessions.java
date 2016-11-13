package se.umu.cs.c12mkn.server.security;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by currybullen on 11/13/16.
 */
public class Sessions {
    private static final Sessions instance = new Sessions();

    private final Map<String, Boolean> sessions;

    private Sessions() {
        sessions = new HashMap<String, Boolean>();
    }

    public static Sessions getInstance() {
        return instance;
    }

    public String createSession() {
        String id = UUID.randomUUID().toString();
        sessions.put(id, false);
        return id;
    }

    public void validateSession(String id) throws NoSuchSessionException {
        if (sessions.get(id) != null)
            sessions.put(id, true);
        else
            throw new NoSuchSessionException("No session exists for session id '" + id + "'.");
    }

    public boolean isValidSession(String id) {
        if (!sessions.containsKey(id))
            return false;
        if (sessions.get(id))
            return true;
        return false;
    }
}
