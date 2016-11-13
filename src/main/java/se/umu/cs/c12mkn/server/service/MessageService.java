package se.umu.cs.c12mkn.server.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import se.umu.cs.c12mkn.grpc.*;
import se.umu.cs.c12mkn.server.security.Secrets;
import se.umu.cs.c12mkn.server.security.Sessions;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.util.logging.Logger;

/**
 * Created by currybullen on 11/12/16.
 */
public class MessageService extends MessageServiceGrpc.MessageServiceImplBase {
    private static final Logger logger = Logger.getLogger(MessageService.class.getName());

    @Override
    public void dHKeyExchange(DHParameters dhParameters, StreamObserver<SignedDHResponse> responseObserver) {
        KeyPair keyPair = Secrets.getInstance().generateKeyPair(dhParameters.getModulus().toByteArray(),
                dhParameters.getBase().toByteArray());
        SecretKey secretKey = Secrets.getInstance().generateSecret(keyPair,
                dhParameters.getPublicKey().toByteArray(),
                dhParameters.getAlgorithm());
        String session = Sessions.getInstance().createSession();
        Secrets.getInstance().addSecret(session, secretKey);
        PublicKey publicKey = PublicKey.newBuilder().setKey(ByteString.copyFrom(keyPair.getPublic().getEncoded()))
                .setSession(session)
                .build();
//        SignedDHResponse signedDHResponse = SignedDHResponse.newBuilder().setPublicKey(publicKey).
//                setSign()
    }

//    @Override
//    public void initAuth(Username username, StreamObserver<Challenge> responseObserver) {
//        try {
//            String challenge = Challenges.getInstance().getChallenge(username.getValue());
//            responseObserver.onNext(Challenge.newBuilder().setValue(challenge).build());
//        } catch (InvalidUserException e) {
//            logger.info(e.getMessage());
//            e.printStackTrace();
//        } catch (NoChallengesException e) {
//            logger.info(e.getMessage());
//            e.printStackTrace();
//        }
//
//        responseObserver.onCompleted();
//    }
//
//    @Override
//    public void authenticate(Answer answer, StreamObserver<PublicKey> responseObserver) {
//        Challenges.getInstance().validateAnswer()
//    }
//
//    @Override
//    public void postMessage(EncryptedMessage encryptedMessage, StreamObserver<Empty> responseObserver) {
//
//    }
}
