public class Message {
    private int nodeId;
    private VectorClock vectorClock;
    private LamportTimestamp lamportTimestamp;
    private String message;

    public Message(int nodeId, VectorClock vectorClock, String message) {
        this.nodeId = nodeId;
        this.vectorClock = vectorClock;
        this.message = message;
    }

    public Message(int nodeId, LamportTimestamp lamportTimestamp, String message) {
        this.nodeId = nodeId;
        this.lamportTimestamp = lamportTimestamp;
        this.message = message;
    }

    public int getNodeId() {
        return nodeId;
    }

    public VectorClock getVectorClock() {
        return vectorClock;
    }

    public LamportTimestamp getLamportTimestamp() {
        return lamportTimestamp;
    }

    public String getMessage() {
        return message;
    }
}
