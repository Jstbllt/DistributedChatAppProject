import java.util.*;

public class Node {
    private int nodeId;
    private VectorClock vectorClock;
    private MatrixClock matrixClock;
    private LamportTimestamp lamportTimestamp;
    private ArrayList<Message> delayedMessages;
    private Map<Integer, Node> neighbors;

    // Constructor
    public Node(int nodeId) {
        this.nodeId = nodeId;
        this.neighbors = new HashMap<>();
        this.vectorClock = new VectorClock(5);
        this.matrixClock = new MatrixClock(5);
        this.lamportTimestamp = new LamportTimestamp();
        this.delayedMessages = new ArrayList<Message>();
    }

    // Permits to add neighbors to the node network
    public void addNeighbor(Node neighbor) {
        neighbors.put(neighbor.getNodeId(), neighbor);
    }

    // Getter of the node's id
    public int getNodeId() {
        return nodeId;
    }

    // Getter of the node's VectorClock when there is one
    public VectorClock getVectorClock() { return vectorClock; }

    // SEND BROADCAST MESSAGE
    public void sendBroadcastMessage(String message) {
        // VECTOR CLOCK without causality PART --------------------------
            /*vectorClock.increment(nodeId);

            Message broadcastMessage = new Message(nodeId, vectorClock, message);

            System.out.printf("Node %d send broadcast message to all: %s " + vectorClock + "\n",
                    nodeId, broadcastMessage.getMessage());

            for (Node neighbor : neighbors.values()) {
                neighbor.receiveBroadcastMessage(broadcastMessage);
            }*/

        // MATRIX CLOCK with causality PART -----------------------------
        matrixClock.increment(nodeId, nodeId);

        for (Node neighbor : neighbors.values()) {
            matrixClock.increment(nodeId, neighbor.nodeId);
        }

        Message broadcastMessage = new Message(nodeId, matrixClock, message);

        System.out.printf("Node %d send broadcast message to all: %s " + matrixClock + "\n",
                nodeId, broadcastMessage.getMessage());

        for (Node neighbor : neighbors.values()) {
            neighbor.receiveBroadcastMessage(broadcastMessage);
        }
    }

    // RECEIVE BROADCAST MESSAGE
    public void receiveBroadcastMessage(Message message) {
        // VECTOR CLOCK without causality PART --------------------------
            /*vectorClock.increment(nodeId);

            for( int i = 0 ; i < vectorClock.getClock().length ; i++){
                if(i + 1 != nodeId){
                    vectorClock.setClock(i, Math.max(vectorClock.getClock()[i], message.getVectorClock().getClock()[i]));
                }
            }

            System.out.println("Node " + nodeId + " received broadcast message from Node " + message.getNodeId() +
                    ": " + message.getMessage() + " " + vectorClock);*/

        // MATRIX CLOCK with causality PART -----------------------------
        if(checkMessage(message)){
            System.out.println("Message processed");
            processMessage(message);


            if(!delayedMessages.isEmpty())
            {
                System.out.println("Entered if loop");
                for (int i = 0 ; i < delayedMessages.size() ; i++) {
                    Message m = delayedMessages.get(i);
                    System.out.println("Entered for loop");
                    if (checkMessage(m)) {
                        System.out.println("m checked");
                        processMessage(m);
                        delayedMessages.remove(m);
                    }
                }
            }
        }
        else
            delayedMessages.add(message);

    }

    // Check whether a message can be delivered or need to be delayed
    private boolean checkMessage(Message message){
        boolean toDeliver = false;
        if(message.getMatrixClock().getClock()[message.getNodeId()-1][nodeId-1] == (matrixClock.getClock()[message.getNodeId()-1][nodeId-1] + 1)){
            toDeliver = true;
            for( int i = 0 ; i < vectorClock.getClock().length ; i++){
                if(i + 1 != nodeId && i + 1 != message.getNodeId()) {
                    if(message.getMatrixClock().getClock()[i][nodeId-1] <= matrixClock.getClock()[i][nodeId-1]){
                        toDeliver = toDeliver && true;
                    }
                    else
                        toDeliver = toDeliver && false;
                }
            }
        }
        return toDeliver;
    }

    // Process by delivering the message and updating clocks
    private void processMessage(Message message){
        matrixClock.increment(nodeId, nodeId);
        matrixClock.increment(message.getNodeId(), nodeId);

        for( int i = 0 ; i < matrixClock.getClock().length ; i++){
            if(i + 1 != nodeId && i + 1 != message.getNodeId()){
                for( int j = 0 ; j < matrixClock.getClock().length ; j++){
                    if(j + 1 != nodeId && j + 1 != message.getNodeId()){
                        matrixClock.setClock(i,i, Math.max(matrixClock.getClock()[i][j], message.getMatrixClock().getClock()[i][j]));
                    }
                }
            }
        }

        System.out.println("Node " + nodeId + " received broadcast message from Node " + message.getNodeId() +
                ": " + message.getMessage() + " " + matrixClock);
    }

    // SEND PRIVATE MESSAGE
    public void sendPrivateMessage(String message, Node recipient) {
        lamportTimestamp.increment();

        Message privateMessage = new Message(nodeId, lamportTimestamp.copy(), message);

        System.out.println("Node " + nodeId + " send private message to Node " + recipient.getNodeId() +
                ": " + privateMessage.getMessage() + " " + lamportTimestamp + " of Node " + nodeId);

        recipient.receivePrivateMessage(privateMessage);
    }

    // RECEIVE PRIVATE MESSAGE
    public void receivePrivateMessage(Message message) {
        lamportTimestamp.update(message.getLamportTimestamp());
        System.out.println("Node " + nodeId + " received private message from Node " + message.getNodeId() +
                ": " + message.getMessage() + " " + lamportTimestamp + " of Node " + nodeId);
    }
}
