public class LamportTimestamp {
    private int timestamp;

    public LamportTimestamp() {
        this.timestamp = 0;
    }

    public int getTimestamp(){
        return timestamp;
    }

    public void increment() {
        timestamp++;
    }

    public void update(LamportTimestamp other) {
        timestamp = Math.max(timestamp, other.timestamp) + 1;
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
