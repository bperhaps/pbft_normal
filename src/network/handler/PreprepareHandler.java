package network.handler;

import model.blockchain.Block;
import model.crypto.Credential;
import model.message.Prepare;
import model.message.Preprepare;
import model.message.Request;
import model.message.SignedMessage;
import model.state.States;
import network.Network;
import repository.Repositories;

import java.util.Base64;

public class PreprepareHandler implements MessageHandler {
    @Override
    public void handle(SignedMessage signedMessage, Network network, States states, Repositories repositories, Credential credential) {
        //프라이머리 검증 록직
        Preprepare preprepare = (Preprepare) signedMessage.getMessage();
        Block block = signedMessage.getPiggiBack();

        if (!preprepare.isSameBlockHash(block)) {
            System.out.println("ERROR preprepare: message digest is not equals");
            return;
        }

        if (!states.getView().compareWith(preprepare.getView())) {
            System.out.println("ERROR preprepare: view");
            return;
        }

        Preprepare storedPrprepare = repositories
                .preprepare().find(preprepare.getIdentity());

        if (storedPrprepare != null && !storedPrprepare.equals(preprepare)) {
            System.out.println("Error preprepare: preprepare already exist in same view and sequence");
            return;
        }

        if (!states.getWaterMark().isBetweenIn(preprepare.getSequenceNumber())) {
            System.out.println("ERROR preprepare: sequenceNumber");
            return;
        }

        repositories.preprepare().add(preprepare.getIdentity(), preprepare);
        repositories.tempBlock().add(block.getCurrentHash(), block);

        Prepare prepare = new Prepare(preprepare, credential);

        SignedMessage signedPrepare = SignedMessage.of(prepare, credential);
        network.broadcast(signedPrepare);
        new Thread(() -> Operation.handle(signedPrepare, network, states, repositories, credential)).start();
    }
}
