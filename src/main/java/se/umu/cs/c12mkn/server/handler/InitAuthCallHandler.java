package se.umu.cs.c12mkn.server.handler;

import com.google.protobuf.InvalidProtocolBufferException;
import se.umu.cs.c12mkn.grpc.Challenge;
import se.umu.cs.c12mkn.grpc.EncryptedMessage;
import se.umu.cs.c12mkn.grpc.Username;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Challenges;
import se.umu.cs.c12mkn.server.security.InvalidUserException;
import se.umu.cs.c12mkn.server.security.NoChallengesException;

import java.util.logging.Logger;

/**
 * Created by currybullen on 11/16/16.
 */
public class InitAuthCallHandler extends CallHandler {
    private static final Logger logger = Logger.getLogger(InitAuthCallHandler.class.getName());

    public EncryptedMessage handle(EncryptedMessage encryptedMessage) {
        EncryptedMessage response = null;
        String session = encryptedMessage.getSession();
        logger.info("Authentication intiated by session '" + session + "'.");
        try {
            Username username = Username.parseFrom(decryptMessage(encryptedMessage));
            String challengeString = Challenges.getInstance().getChallenge(username.getValue());
            Challenge challenge = MessageBuilder.buildChallengeMessage(challengeString);
            response = encryptMessage(challenge.toByteArray(), session, encryptedMessage.getAlgorithm());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        } catch (InvalidUserException e) {
            e.printStackTrace();
        } catch (NoChallengesException e) {
            e.printStackTrace();
        }

        return response;
    }

//    @Override
//    public void initAuth(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
//        String session = encryptedMessage.getSession();
//        logger.info("Authentication intiated by session '" + session + "'.");
//        byte[] decrypted = Crypt.decrypt(encryptedMessage.toByteArray(), getSecretKey(session));
//        try {
//            Username username = Username.parseFrom(decrypted);
//            String challenge = Challenges.getInstance().getChallenge(username.getValue());
//            EncryptedMessage response = messageBuilder.buildChallengeMessage(challenge, session);
//            responseObserver.onNext(response);
//            logger.info("Responded to authentication initiation.");
//        } catch (Exception e) {
//            logger.info("Could not initiate authentication.");
//            e.printStackTrace();
//        } catch (NoChallengesException e) {
//            logger.info("Bad format on authentication request.");
//            e.printStackTrace();
//        }
//
//        responseObserver.onCompleted();
//    }
}
