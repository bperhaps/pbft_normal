package model.message;

import model.crypto.Credential;

import java.io.Serializable;

public class Prepare extends ConsensusMessage implements Serializable {

    public Prepare(Preprepare preprepare, Credential credential) {
        super(
                preprepare.getSequenceNumber(),
                preprepare.getView(),
                preprepare.getBlockHash(),
                credential.getPrimaryNumber()
        );
    }
}
