package network.handler;

import model.blockchain.service.BlockGenerator;
import model.crypto.Credential;
import model.message.Commit;
import model.message.SignedMessage;
import model.state.NetworkState;
import model.state.States;
import model.state.View;
import model.state.WaterMark;

import network.Network;
import repository.Repositories;


public class CommitHandler implements MessageHandler {
    @Override
    public void handle(SignedMessage signedMessage, Network network, States states, Repositories repositories, Credential credential) {
        Commit commit = (Commit) signedMessage.getMessage();

        if (!states.getView().compareWith(commit.getView())) {
            System.out.println("ERROR commit: view");
            return;
        }

        Commit storedCommit = repositories
                .commit().find(commit.getIdentity());

        if (storedCommit != null && !storedCommit.equals(commit)) {
            System.out.println("Error commit: commit already exist in same view and sequence");
            return;
        }

        if (!states.getWaterMark().isBetweenIn(commit.getSequenceNumber())) {
            System.out.println("ERROR commit: sequenceNumber");
            return;
        }

        repositories.commit().add(commit.getIdentity(), commit);

        synchronized (this) {
            if (repositories.commit().isCommitted(commit, states, repositories) &&
                    !states.getNetworkState().isSentMessage(commit.getIdentity())) {
                BlockGenerator.findBlockAndAddBlock(commit, repositories, credential);
                states.getNetworkState().sentMessage(commit.getIdentity());
            }
        }
    }
}
