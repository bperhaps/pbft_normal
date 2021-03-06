package model.state;

public class View {
    private long view;
    private NetworkState networkState;

    public View(NetworkState networkState) {
        this.networkState = networkState;
    }

    public void add() {
        view++;
    }

    public long get() {
        return view;
    }

    public long getPrimary() {
        return view % networkState.getN();
    }

    public boolean compareWith(long view) {
        return this.view == view;
    }
}
