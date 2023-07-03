import java.util.*;

public class Node {
    private int nodeId;
    private VectorClock vectorClock;
    private MatrixClock matrixClock;
    private LamportTimestamp lamportTimestamp;
    private ArrayList<Message> delayedMessages;
    private ArrayList<Message> delivered = new ArrayList<>();
    private Map<Integer, Node> neighbors;

    // Constructor
    public Node(int nodeId) {
        this.nodeId = nodeId;
        this.neighbors = new HashMap<>();
        this.vectorClock = new VectorClock(5);
        this.matrixClock = new MatrixClock(5);
        this.lamportTimestamp = new LamportTimestamp();
        this.delayedMessages = new ArrayList<>();
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
        VectorClock deps = vectorClock.copy();
        Message broadcastMessage = new Message(nodeId, deps, message);

        System.out.printf("Node %d send broadcast message to all: %s " + vectorClock + "\n",
                nodeId, broadcastMessage.getMessage());

        for (Node neighbor : neighbors.values()) {
            neighbor.receiveBroadcastMessage(broadcastMessage);
        }

        vectorClock.increment(nodeId);

        // MATRIX CLOCK with causality PART -----------------------------
        /*matrixClock.increment(nodeId, nodeId);

        for (Node neighbor : neighbors.values()) {
            matrixClock.increment(nodeId, neighbor.nodeId);
        }

        Message broadcastMessage = new Message(nodeId, matrixClock, message);

        System.out.printf("Node %d send broadcast message to all: %s " + matrixClock + "\n",
                nodeId, broadcastMessage.getMessage());

        for (Node neighbor : neighbors.values()) {
            neighbor.receiveBroadcastMessage(broadcastMessage);
        }*/
    }

    // RECEIVE BROADCAST MESSAGE
    public void receiveBroadcastMessage(Message message) {
        // VECTOR CLOCK without causality PART --------------------------
        /*if(checkVectorMessage(message)){
            vectorClock.increment(message.getNodeId());
            System.out.println("Node " + nodeId + " received broadcast message from Node " + message.getNodeId() +
                    ": " + message.getMessage() + " " + vectorClock);

            if(!delayedMessages.isEmpty())
            {
                for (int i = 0 ; i < delayedMessages.size() ; i++) {
                    Message m = delayedMessages.get(i);
                    if (checkVectorMessage(m)) {
                        vectorClock.increment(m.getNodeId());
                        System.out.println("Node " + nodeId + " received broadcast message from Node " + m.getNodeId() +
                                ": " + m.getMessage() + " " + vectorClock);
                        delayedMessages.remove(m);
                    }
                }
            }
        }
        else
            delayedMessages.add(message);*/

        // MATRIX CLOCK with causality PART -----------------------------
        if(checkMatrixMessage(message)){
            processMatrixMessage(message);

            if(!delayedMessages.isEmpty())
            {
                for (int i = 0 ; i < delayedMessages.size() ; i++) {
                    Message m = delayedMessages.get(i);
                    if (checkMatrixMessage(m)) {
                        processMatrixMessage(m);
                        delayedMessages.remove(m);
                    }
                }
            }
        }
        else
            delayedMessages.add(message);

    }

    // Check whether a message can be delivered or need to be delayed
    private boolean checkMatrixMessage(Message message){
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

    private boolean checkVectorMessage(Message message){
        for (int i = 0; i < message.getVectorClock().getClock().length; i++) {
            if (message.getVectorClock().getClock()[i] > vectorClock.getClock()[i] + 1) {
                return false;
            }
        }
        return true;
    }

    // Process by delivering the message and updating clocks
    private void processMatrixMessage(Message message){
        matrixClock.increment(nodeId, nodeId);
        matrixClock.increment(message.getNodeId(), nodeId);

        for( int i = 0 ; i < matrixClock.getClock().length ; i++){
            for( int j = 0 ; j < matrixClock.getClock().length ; j++){
                if(!(i + 1 == nodeId && j + 1 == nodeId) && !(i + 1 == message.getNodeId() && j + 1 == nodeId)){
                    matrixClock.setClock(i, j, Math.max(matrixClock.getClock()[i][j], message.getMatrixClock().getClock()[i][j]));
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
        if(delivered.isEmpty() ||
                message.getLamportTimestamp().getTimestamp() == delivered.get(delivered.size()-1).getLamportTimestamp().getTimestamp() + 1) {

            delivered.add(message);

            lamportTimestamp.update(message.getLamportTimestamp());

            System.out.println("Node " + nodeId + " received private message from Node " + message.getNodeId() +
                    ": " + message.getMessage() + " " + lamportTimestamp + " of Node " + nodeId);

            if(!delayedMessages.isEmpty()){
                for (int i = 0 ; i < delayedMessages.size() ; i++) {
                    Message m = delayedMessages.get(i);
                    if (m.getLamportTimestamp().getTimestamp() == delivered.get(delivered.size()-1).getLamportTimestamp().getTimestamp() + 1) {
                        delivered.add(m);
                        lamportTimestamp.update(m.getLamportTimestamp());
                        System.out.println("Node " + nodeId + " received private message from Node " + m.getNodeId() +
                                ": " + m.getMessage() + " " + lamportTimestamp + " of Node " + nodeId);
                        delayedMessages.remove(m);
                    }
                }
            }
        }
        else
            delayedMessages.add(message);
    }
}
