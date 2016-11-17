package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.AuthResponse;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Challenges;
import se.umu.cs.c12mkn.server.security.NoSuchSessionException;
import se.umu.cs.c12mkn.server.security.Sessions;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class AuthenticateHandler extends CallHandler {
    private static final Logger logger =
            Logger.getLogger(AuthenticateHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        try {
            AuthResponse authResponse = AuthResponse.parseFrom(decryptMessage(request));
            logger.info("Received authentication response from session '" +
                    request.getSession() + "'.");
            if (validateAnswer(authResponse)) {
                logger.info("Authentication succeeded!");
                Sessions.getInstance().validateSession(request.getSession());
                return buildEncryptedSuccessMessage(true, request.getSession());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (NoSuchSessionException e) {
            e.printStackTrace();
        }

        logger.info("Authentication failed!");
        return buildEncryptedSuccessMessage(false, request.getSession());
    }

    private boolean validateAnswer(AuthResponse authResponse) {
        String username = authResponse.getUsername();
        String challenge = authResponse.getChallenge();
        String answer = authResponse.getAnswer();
        return Challenges.getInstance().validateAnswer(username, challenge,
                answer);
    }
}
