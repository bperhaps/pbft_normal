package model.message;

import model.crypto.Credential;

public class Commit extends ConsensusMessage {
    public Commit(Prepare prepare, Credential credential) {
        super(
                prepare.getSequenceNumber(),
                prepare.getView(),
                prepare.getBlockHash(),
                credential.getPrimaryNumber()
        );
    }

}
