package se.umu.cs.c12mkn.server.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by currybullen on 11/13/16.
 */
public class Challenges {
    private static final Challenges instance = new Challenges();

    private Map<String, Map<String, String>> challenges;
    //private HashMap<String, List<String>> challenges;

    private Challenges() {
        challenges = new HashMap<String, Map<String, String>>();

        challenges.put("currybullen", new HashMap<String, String>());
        challenges.get("currybullen").put("nkSW4rs5", "ZfDPxY5Y");
        challenges.get("currybullen").put("9c5SKUaH", "Upr2BRZT");
        challenges.get("currybullen").put("tGuR7HjX", "knbJtHAL");
        challenges.get("currybullen").put("P85yPhJ3", "hehAGbwf");
        challenges.get("currybullen").put("axKgbUnG", "YSwSyqXp");

        challenges.put("paprikafix", new HashMap<String, String>());
        challenges.get("paprikafix").put("S6YfsQ5N", "j3q9MxNZ");
        challenges.get("paprikafix").put("AFuDBTng", "Ts7wPeZy");
        challenges.get("paprikafix").put("b3Bb7fyW", "DNVzBcrg");
        challenges.get("paprikafix").put("YuRXWgky", "gFw5z3J9");
        challenges.get("paprikafix").put("AeSqavYb", "J9LgD4mh");
    }

    public static Challenges getInstance() {
        return instance;
    }

    public synchronized String getChallenge(String username) throws InvalidUserException, NoChallengesException {
        if (challenges.get(username) == null)
            throw new InvalidUserException("No such user '" + username  + "'.");
        if (challenges.get(username).isEmpty())
            throw new NoChallengesException("No more challenges available for '" + username + "'.");
        return new ArrayList<String>(challenges.get(username).keySet()).get(0);
    }

    public synchronized boolean validateAnswer(String username, String challenge, String answer) {
        if (challenges.get(username) == null)
            return false;
        if (challenges.get(username).get(challenge) == null)
            return false;
        if (challenges.get(username).get(challenge).equals(answer)) {
            challenges.get(username).remove(challenge);
            return true;
        }

        return false;
    }
}
