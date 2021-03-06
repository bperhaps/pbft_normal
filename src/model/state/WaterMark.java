package model.state;

public class WaterMark {
    private static final long INTERVAL = Integer.MAX_VALUE;

    private long low;

    public WaterMark() {
    }

    public boolean isBetweenIn(long sequenceNumber) {
        return low <= sequenceNumber && sequenceNumber <= low + INTERVAL;
    }
}
