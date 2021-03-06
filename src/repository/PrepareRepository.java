package repository;

import model.message.ConsensusMessage;
import model.message.Prepare;
import model.state.States;

public class PrepareRepository extends Repository<Prepare> {

    public boolean isPrepared(ConsensusMessage prepare, States states, Repositories repositories) {
        return isOverThreshold(prepare, states) && hasSamePreprepare(prepare, states, repositories);
    }
}
