public class Message {
    private int nodeId;
    private VectorClock vectorClock;
    private MatrixClock matrixClock;
    private LamportTimestamp lamportTimestamp;
    private String message;

    // 3 types of constructor according to the clock type used
    public Message(int nodeId, VectorClock vectorClock, String message) {
        this.nodeId = nodeId;
        this.vectorClock = vectorClock;
        this.message = message;
    }
    public Message(int nodeId, MatrixClock matrixClock, String message) {
        this.nodeId = nodeId;
        this.matrixClock = matrixClock;
        this.message = message;
    }

    public Message(int nodeId, LamportTimestamp lamportTimestamp, String message) {
        this.nodeId = nodeId;
        this.lamportTimestamp = lamportTimestamp;
        this.message = message;
    }

    // Getters
    public int getNodeId() {
        return nodeId;
    }
    public VectorClock getVectorClock() {
        return vectorClock;
    }
    public MatrixClock getMatrixClock() { return matrixClock; }
    public LamportTimestamp getLamportTimestamp() {
        return lamportTimestamp;
    }
    public String getMessage() {
        return message;
    }
}
