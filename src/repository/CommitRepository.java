package repository;

import model.message.Commit;
import model.message.Prepare;
import model.state.States;

public class CommitRepository extends Repository<Commit> {
    public boolean isCommitted(Commit commit, States states, Repositories repositories) {
        return isOverThreshold(commit, states) && isPrepared(commit, states, repositories);
    }

    private boolean isPrepared(Commit commit, States states, Repositories repositories) {
        Prepare prepare = getPrepare(commit, states, repositories);
        if (prepare == null) {
            return false;
        }
        return repositories.prepare().isPrepared(prepare, states, repositories);
    }

    private Prepare getPrepare(Commit commit, States states, Repositories repositories) {
        String key = createIdentityWithPrimaryNumber(commit.getPrimaryNumber(), commit, Prepare.class.getName());
        for (int i = 0; i < states.getNetworkState().getN(); i++) {
            Prepare prepare = repositories.prepare().find(key);
            if (prepare != null) {
                return prepare;
            }
        }

        return null;
    }
}
