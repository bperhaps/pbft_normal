package network.handler;

import model.crypto.Credential;
import model.message.SignedMessage;
import model.state.States;
import network.Network;
import repository.Repositories;

public interface MessageHandler {
    void handle(SignedMessage signedMessage, Network network, States states, Repositories repositories, Credential credential);
}
