package network.handler;

import model.crypto.Credential;
import model.message.SignedMessage;
import model.state.States;
import network.Network;
import repository.Repositories;

public class ReplyHandler implements MessageHandler {
    @Override
    public void handle(SignedMessage signedMessage, Network network, States states, Repositories repositories, Credential credential) {

    }
}
