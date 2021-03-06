package model.message;

import model.blockchain.Block;
import network.Network;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;

public abstract class ConsensusMessage extends Message implements Serializable {
    private long view;
    private long sequenceNumber;
    private byte[] blockHash;
    private long primaryNumber;

    public ConsensusMessage(long sequenceNumber, long view, byte[] blockHash, long primaryNumber) {
            this.sequenceNumber = sequenceNumber;
            this.view = view;
            this.blockHash = blockHash;
            this.primaryNumber = primaryNumber;
    }

    public long getPrimaryNumber() {
        return primaryNumber;
    }

    public long getView() {
        return view;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public byte[] getBlockHash() {
        return blockHash;
    }

    public boolean isSameBlockHash(Block block) {
        return Arrays.compare(block.getCurrentHashBytes(), blockHash) == 0;
    }

    public String getIdentity() {
        return String.join(Network.DELIMITER,
                String.valueOf(primaryNumber),
                String.valueOf(view),
                String.valueOf(sequenceNumber),
                getClass().getName(),
                Base64.getEncoder().encodeToString(blockHash)
                );
    }

    @Override
    public boolean equals(Object o) {
        ConsensusMessage m = (ConsensusMessage) o;
        return m.view == this.view &&
                m.sequenceNumber == this.sequenceNumber &&
                Arrays.compare(this.blockHash, m.blockHash) == 0;

    }
}
