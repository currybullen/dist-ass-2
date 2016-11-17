package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.Challenge;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Username;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Challenges;
import se.umu.cs.c12mkn.server.security.exception.NoChallengesException;
import se.umu.cs.c12mkn.server.security.exception.NoSuchUsernameException;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class InitAuthHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(InitAuthHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage encryptedMessage) {
        String session = encryptedMessage.getSession();
        logger.info("Authentication intiated by session '" + session + "'.");
        try {
            Username username = Username.parseFrom(decryptMessage(encryptedMessage));
            String challengeString = Challenges.getInstance().getChallenge(username.getValue());
            Challenge challenge = MessageBuilder.buildChallengeMessage(challengeString);
            return encryptMessage(challenge.toByteArray(), session);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (NoSuchUsernameException e) {
            e.printStackTrace();
        } catch (NoChallengesException e) {
            e.printStackTrace();
        }

        return null;
    }
}
