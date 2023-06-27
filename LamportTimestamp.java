public class LamportTimestamp {
    private int timestamp;

    public LamportTimestamp() {
        this.timestamp = 0;
    }

    public void increment() {
        timestamp++;
    }

    public void update(LamportTimestamp other) {
        timestamp = Math.max(timestamp, other.timestamp);
    }

    public LamportTimestamp copy() {
        LamportTimestamp copy = new LamportTimestamp();
        copy.timestamp = timestamp;
        return copy;
    }

    @Override
    public String toString() {
        return "LamportTimestamp = " + timestamp;
    }
}
