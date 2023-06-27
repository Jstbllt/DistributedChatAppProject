import java.util.*;

public class Node {
    private int nodeId;
    private int sendSeq;
    private VectorClock vectorClock; //delivered
    private ArrayList<Message> buffer;
    private LamportTimestamp lamportTimestamp;
    private Map<Integer, Node> neighbors;

    private int index;

    public Node(int nodeId) {
        this.nodeId = nodeId;
        this.sendSeq = 0;
        this.vectorClock = new VectorClock();
        this.buffer = new ArrayList<Message>();
        this.lamportTimestamp = new LamportTimestamp();
        this.neighbors = new HashMap<>();
    }

    public void addNeighbor(Node neighbor) {
        neighbors.put(neighbor.getNodeId(), neighbor);
    }

    // SEND and RECEIVE BROADCAST
    public void sendBroadcastMessage(String message) {

        VectorClock deps = this.vectorClock.copy();
        deps.setClock(this.nodeId -1, this.sendSeq);

        Message broadcastMessage = new Message(nodeId, deps, message);

        System.out.printf("Node %d send broadcast message to all: %s " + vectorClock + "\n",
                nodeId, broadcastMessage.getMessage());

        for (Node neighbor : neighbors.values()) {
            neighbor.receiveBroadcastMessage(broadcastMessage);
        }

        this.sendSeq++;
    }

    public boolean canBeDelivered(){
        boolean canBeDelivered = true;
        index = 0;
        for(Message bufferMessage : this.buffer) {
            index = buffer.indexOf(bufferMessage);
            for(int i = 0 ; i <= bufferMessage.getVectorClock().getClock().length -1 ; i++){
                if(bufferMessage.getVectorClock().getClock()[i] > vectorClock.getClock()[i]){
                    canBeDelivered = false;
                    break;
                }
                else
                    canBeDelivered = true;
            }
        }
        return canBeDelivered;
    }

    public void receiveBroadcastMessage(Message message) {
        this.buffer.add(message);

        while (!buffer.isEmpty() && canBeDelivered()){
            int senderId = buffer.get(index).getNodeId();
            this.vectorClock.setClock(senderId - 1, this.vectorClock.getClock()[senderId-1]+1);

            System.out.println("Node " + nodeId + " received broadcast message from Node " + senderId +
                    ": " + buffer.get(index).getMessage() + " " + vectorClock);

            this.buffer.remove(index);

        }
    }


    // SEND and RECEIVE PRIVATE
    public void sendPrivateMessage(String message, Node recipient) {
        lamportTimestamp.increment();

        Message privateMessage = new Message(nodeId, lamportTimestamp.copy(), message);

        System.out.println("Node " + nodeId + " send private message to Node " + recipient.getNodeId() +
                ": " + privateMessage.getMessage() + " " + lamportTimestamp + " of Node " + nodeId);

        recipient.receivePrivateMessage(privateMessage);
    }

    public void receivePrivateMessage(Message message) {
        lamportTimestamp.update(message.getLamportTimestamp());
        System.out.println("Node " + nodeId + " received private message from Node " + message.getNodeId() +
                ": " + message.getMessage() + " " + lamportTimestamp + " of Node " + nodeId);
    }

    public int getNodeId() {
        return nodeId;
    }
}
