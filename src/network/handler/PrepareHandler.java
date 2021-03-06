package network.handler;

import model.crypto.Credential;
import model.message.Commit;
import model.message.Prepare;
import model.message.SignedMessage;
import model.state.NetworkState;
import model.state.States;
import model.state.View;
import model.state.WaterMark;
import network.Network;
import repository.Repositories;

public class PrepareHandler implements MessageHandler {
    @Override
    public void handle(SignedMessage signedMessage, Network network, States states, Repositories repositories, Credential credential) {
        Prepare prepare = (Prepare) signedMessage.getMessage();

        if (!states.getView().compareWith(prepare.getView())) {
            System.out.println("ERROR preprepare: view");
            return;
        }

        Prepare storedPrepare = repositories
                .prepare().find(prepare.getIdentity());

        if (storedPrepare != null && !storedPrepare.equals(prepare)) {
            System.out.println("Error prepare: prepare already exist in same view and sequence");
            return;
        }

        if (!states.getWaterMark().isBetweenIn(prepare.getSequenceNumber())) {
            System.out.println("ERROR preprepare: sequenceNumber");
            return;
        }

        repositories.prepare().add(prepare.getIdentity(), prepare);


        synchronized (this) {
            if (repositories.prepare().isPrepared(prepare, states, repositories) &&
                    !states.getNetworkState().isSentMessage(prepare.getIdentity())) {
                Commit commit = new Commit(prepare, credential);
                SignedMessage signedPrepare = SignedMessage.of(commit, credential);
                network.broadcast(signedPrepare);
                new Thread(() -> Operation.handle(signedPrepare, network, states, repositories, credential)).start();
                states.getNetworkState().sentMessage(prepare.getIdentity());
            }
        }
    }
}