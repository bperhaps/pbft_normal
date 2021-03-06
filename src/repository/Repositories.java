package repository;

import java.awt.*;

public class Repositories {
    private PublicKeyRepository publicKeyRepository;
    private PreprepareRepository preprepareRepository;
    private PrepareRepository prepareRepository;
    private CommitRepository commitRepository;
    private RequestRepository requestRepository;
    private BlockRepository blockRepository;
    private TempBlockRepository tempBlockRepository;

    public Repositories() {
        this.publicKeyRepository = new PublicKeyRepository();
        this.preprepareRepository = new PreprepareRepository();
        this.prepareRepository = new PrepareRepository();
        this.commitRepository = new CommitRepository();
        this.requestRepository = new RequestRepository();
        this.blockRepository = new BlockRepository();
        this.tempBlockRepository = new TempBlockRepository();
    }

    public PublicKeyRepository publicKey() {
        return publicKeyRepository;
    }

    public PreprepareRepository preprepare() {
        return preprepareRepository;
    }

    public PrepareRepository prepare() {
        return prepareRepository;
    }

    public CommitRepository commit() {
        return commitRepository;
    }

    public RequestRepository request() {
        return requestRepository;
    }

    public BlockRepository block() {
        return blockRepository;
    }

    public TempBlockRepository tempBlock() {
        return tempBlockRepository;
    }
}
