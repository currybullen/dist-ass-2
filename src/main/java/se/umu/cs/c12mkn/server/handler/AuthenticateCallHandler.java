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
public class AuthenticateCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(AuthenticateCallHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage request) {
        try {
            AuthResponse authResponse = AuthResponse.parseFrom(decryptMessage(request));
            logger.info("Received authentication response from session '" + request.getSession() + "'.");
            if (Challenges.getInstance().validateAnswer(authResponse.getUsername(),
                    authResponse.getChallenge(), authResponse.getAnswer())) {
                logger.info("Authentication succeeded!");
                Sessions.getInstance().validateSession(request.getSession());
                return encryptMessage(MessageBuilder.buildSucceededMessage(true).toByteArray(),
                        request.getSession());
            }

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (NoSuchSessionException e) {
            e.printStackTrace();
        }

        return encryptMessage(MessageBuilder.buildSucceededMessage(false).toByteArray(),
                request.getSession());
    }

}
