package model.state;

public class SequenceNumber {
    private long sequenceNumber;

    public SequenceNumber() { }

    public long get() {
        return sequenceNumber;
    }

    public void add(){
        sequenceNumber++;
    }

}
