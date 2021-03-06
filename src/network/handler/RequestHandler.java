package network.handler;


import model.blockchain.Block;
import model.crypto.Credential;
import model.message.Preprepare;
import model.message.Request;
import model.message.SignedMessage;
import model.state.States;
import network.Network;
import repository.Repositories;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Base64;

public class RequestHandler implements MessageHandler {

    @Override
    public void handle(SignedMessage signedMessage, Network network, States states, Repositories repositories, Credential credential) {
        Request request = (Request) signedMessage.getMessage();

        repositories.request().add(
                        Base64.getEncoder().encodeToString(request.getDigest()),
                        request
                );

        Request[] requests = repositories.request().getRequestsAndDelete();
        if (requests == null) return;

        Block block = null;

        synchronized (this) {
            long newHeight = repositories.block().getHighest() + 1;
            block = new Block(newHeight, requests, repositories.block().getTop());
            states.getSequenceNumber().add();
        }

        repositories.tempBlock().add(block.getCurrentHash(), block);
        Preprepare preprepare = new Preprepare(block.getCurrentHashBytes(), states, credential);
        SignedMessage signedPreprepare = SignedMessage.of(preprepare, block, credential);
        network.broadcast(signedPreprepare);

        new Thread(() ->Operation.handle(signedPreprepare, network, states, repositories, credential)).start();
    }
}
