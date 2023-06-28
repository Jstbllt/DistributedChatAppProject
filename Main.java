import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();

        // Create 5 nodes
        for (int i = 1; i <= 5; i++) {
            Node node = new Node(i);
            nodes.add(node);
        }

        // Add neighbors for each node (assuming a fully connected network)
        for (Node node : nodes) {
            for (Node otherNode : nodes) {
                if (otherNode.getNodeId() != node.getNodeId()) {
                    node.addNeighbor(otherNode);
                }
            }
        }

        // 1 => Simulate broadcast messages exchanges ------------------------------------------
        nodes.get(0).sendBroadcastMessage("Hello, everyone!");
        nodes.get(1).sendBroadcastMessage("Hi Node 1!");
        nodes.get(0).sendBroadcastMessage("How are you all?");
        nodes.get(2).sendBroadcastMessage("Hey");
        nodes.get(3).sendBroadcastMessage("Yo");
        nodes.get(1).sendBroadcastMessage("Fine and you?");
        nodes.get(0).sendBroadcastMessage("Yeah! Let's see us!");

        // 2 => Simulate broadcast messages exchanges with a delayed message -------------------
        /*nodes.get(0).sendBroadcastMessage("Message 1");

        MatrixClock mc = new MatrixClock(5);
        for(int i = 0 ; i < 5 ; i++){
            mc.setClock(0, i,3);
        }
        Message m = new Message(1, mc, "Message 3");
        for (int i = 1; i < 5; i++) {
            nodes.get(i).receiveBroadcastMessage(m);
        }

        nodes.get(0).sendBroadcastMessage("Message 2");*/


        // 3 => Simulate private messages exchanges ---------------------------------------------
        // Conv 1
        /*nodes.get(1).sendPrivateMessage("Hi, there!", nodes.get(0));
        nodes.get(1).sendPrivateMessage("What are u doing tonight?", nodes.get(0));
        nodes.get(1).sendPrivateMessage("Would you come with me to watch Spider-Man at the cinema?", nodes.get(0));
        nodes.get(0).sendPrivateMessage("Hi, nothing! Sure, which hour?", nodes.get(1));

        // Conv 2
        nodes.get(0).sendPrivateMessage("Hey, Node 3!", nodes.get(2));
        nodes.get(0).sendPrivateMessage("How are you?", nodes.get(2));
        nodes.get(2).sendPrivateMessage("Hi! Fine and you Node 1?", nodes.get(0));*/
    }
}
