package se.umu.cs.c12mkn.server.security;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by currybullen on 11/13/16.
 */
public class Sessions {
    private static final Sessions instance = new Sessions();

    private final Map<String, Boolean> ids;
    private final Map<String, String> algorithms;
    private final Map<String, SecretKey> secretKeys;
    private final Map<String, PublicKey> publicKeys;

    private Sessions() {
        ids = new HashMap<String, Boolean>();
        algorithms = new HashMap<String, String>();
        secretKeys = new HashMap<String, SecretKey>();
        publicKeys = new HashMap<String, PublicKey>();
    }

    public static Sessions getInstance() {
        return instance;
    }

    public String createSession(String algortihm, Key key) {
        String id = UUID.randomUUID().toString();
        ids.put(id, false);
        algorithms.put(id, algortihm);
        if (algortihm.equals("AES"))
            secretKeys.put(id, (SecretKey) key);
        else if (algortihm.equals("RSA"))
            publicKeys.put(id, (PublicKey) key);
        return id;
    }

    public void authenticateSession(String id) {
        if (ids.get(id) != null)
            ids.put(id, true);
    }

    public boolean isValidSession(String id) {
        if (!ids.containsKey(id))
            return false;
        if (ids.get(id))
            return true;
        return false;
    }

    public String getAlgorithm(String id) {
        return algorithms.get(id);
    }

    public SecretKey getSecretKey(String id) {
        return secretKeys.get(id);
    }

    public PublicKey getPublicKey(String id) {
        return publicKeys.get(id);
    }
}
