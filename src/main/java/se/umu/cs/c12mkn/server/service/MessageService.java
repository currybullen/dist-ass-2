package se.umu.cs.c12mkn.server.service;

import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.stub.StreamObserver;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.server.MessageBuilder;
import se.umu.cs.c12mkn.server.security.Challenges;
import se.umu.cs.c12mkn.server.security.InvalidUserException;
import se.umu.cs.c12mkn.server.security.NoChallengesException;
import se.umu.cs.c12mkn.server.security.Sessions;
import se.umu.cs.c12mkn.shared.security.Crypt;
import se.umu.cs.c12mkn.shared.security.DHKeyExchange;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    private static final Logger logger = Logger.getLogger(MessageService.class.getName());

    private final MessageBuilder messageBuilder;

    public MessageService() {
        super();
        messageBuilder = new MessageBuilder();
    }

    @Override
    public void dHKeyExchange(DHParameters dhParameters, StreamObserver<SignedDHResponse> responseObserver) {
        logger.info("DH key exchange request received.");
        KeyPair keyPair = DHKeyExchange.generateKeyPair(
                dhParameters.getModulus().toByteArray(),
                dhParameters.getBase().toByteArray());
        SecretKey secretKey = DHKeyExchange.generateSecretKey(keyPair.getPrivate(),
                dhParameters.getPublicKey().toByteArray(),
                dhParameters.getAlgorithm());
        String session = Sessions.getInstance().createSession(secretKey);
        SignedDHResponse signedDHResponse = messageBuilder.buildSignedDHResponse(keyPair.getPublic(), session);
        responseObserver.onNext(signedDHResponse);
        responseObserver.onCompleted();
        logger.info("Responded to DH key exchange request.");
    }

    @Override
    public void initAuth(EncryptedMessage encryptedMessage, StreamObserver<EncryptedMessage> responseObserver) {
        String session = encryptedMessage.getSession();
        logger.info("Authentication intiated by session '" + session + "'.");
        byte[] decrypted = Crypt.decrypt(encryptedMessage.toByteArray(), getSecretKey(session));
        try {
            Username username = Username.parseFrom(decrypted);
            String challenge = Challenges.getInstance().getChallenge(username.getValue());
            EncryptedMessage response = messageBuilder.buildChallengeMessage(challenge, session);
            responseObserver.onNext(response);
            logger.info("Responded to authentication initiation.");
        } catch (Exception e) {
            logger.info("Could not initiate authentication.");
            e.printStackTrace();
        } catch (NoChallengesException e) {
            logger.info("Bad format on authentication request.");
            e.printStackTrace();
        }

        responseObserver.onCompleted();
    }

    private SecretKey getSecretKey(String session) {
        return Sessions.getInstance().getSecretKey(session);
    }
}
