package network.handler;

import model.crypto.Credential;
import model.message.*;
import model.state.States;
import network.Network;
import repository.Repositories;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.Objects;

public enum Operation {
    REQUEST(Request.class.getName(), new RequestHandler()),
    PRE_PREPARE(Preprepare.class.getName(), new PreprepareHandler()),
    PREPARE(Prepare.class.getName(), new PrepareHandler()),
    COMMIT(Commit.class.getName(), new CommitHandler()),
    REPLY(Reply.class.getName(), new ReplyHandler());

    private String name;
    private MessageHandler handler;

    Operation(String name, MessageHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    public static void handle(SignedMessage signedMessage, Network network, States states, Repositories repositories, Credential credential) {
        Operation operation = getOperation(signedMessage.getMessage());

        //System.out.println(credential.primaryNumber + " : " + signedMessage.getMessage().getClass().getName());

        if (operation == null) {
            return;
        }

        PublicKey publicKey = repositories
                .publicKey()
                .find(signedMessage.getSenderName());

        if (!validateSignature(signedMessage, publicKey)) {
            System.out.println("sign not matched");
            return;
        }

        operation.handler.handle(signedMessage, network, states, repositories, credential);
    }

    private static boolean validateSignature(SignedMessage signedMessage, PublicKey publicKey) {
        return Credential.validation(signedMessage, publicKey);
    }

    private static Operation getOperation(Message message) {
        return Arrays.stream(Operation.values())
                .filter(operation ->
                        Objects.equals(message.getClass().getName(), operation.name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException());
    }

}
