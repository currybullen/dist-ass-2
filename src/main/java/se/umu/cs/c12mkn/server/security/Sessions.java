package se.umu.cs.c12mkn.server.security;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by currybullen on 11/13/16.
 */
public class Sessions {
    private static final Sessions instance = new Sessions();

    private final Map<String, Boolean> ids;
    private final Map<String, SecretKey> secretKeys;

    private Sessions() {
        ids = new HashMap<String, Boolean>();
        secretKeys = new HashMap<String, SecretKey>();
    }

    public static Sessions getInstance() {
        return instance;
    }

    public String createSession(SecretKey secretKey) {
        String id = UUID.randomUUID().toString();
        ids.put(id, false);
        secretKeys.put(id, secretKey);
        return id;
    }

    public void validateSession(String id) throws NoSuchSessionException {
        if (ids.get(id) != null)
            ids.put(id, true);
        else
            throw new NoSuchSessionException("No session exists for session id '" + id + "'.");
    }

    public boolean isValidSession(String id) {
        if (!ids.containsKey(id))
            return false;
        if (ids.get(id))
            return true;
        return false;
    }

    public SecretKey getSecretKey(String id) {
        return secretKeys.get(id);
    }
}
