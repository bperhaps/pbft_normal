package model.message;

import model.crypto.Credential;
import model.state.States;

public class Preprepare extends ConsensusMessage {

    public Preprepare(byte[] blockHash, States states, Credential credential) {
        super(states.getSequenceNumber().get(),
                states.getView().get(),
                blockHash,
                credential.getPrimaryNumber()
        );
    }
}
