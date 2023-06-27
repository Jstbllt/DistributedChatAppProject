import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();

        // Create 10 nodes
        for (int i = 1; i <= 10; i++) {
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

        // Simulate broadcast messages exchanges
        nodes.get(0).sendBroadcastMessage("Hello, everyone!");
        nodes.get(1).sendBroadcastMessage("Hi Node 1!");
        nodes.get(2).sendBroadcastMessage("Hey");
        nodes.get(3).sendBroadcastMessage("Yo");

        // Simulate private messages exchanges
        // Conv 1
        /*nodes.get(1).sendPrivateMessage("Hi, there!", nodes.get(0));
        nodes.get(1).sendPrivateMessage("What are u doing tonight?", nodes.get(0));
        nodes.get(1).sendPrivateMessage("Would you come with me to watch Spider Man at the cinema?", nodes.get(0));

        nodes.get(0).sendPrivateMessage("Hi, nothing! Sure, which hour?", nodes.get(1));

        // Conv 2
        nodes.get(0).sendPrivateMessage("Hey, Node 3!", nodes.get(2));
        nodes.get(0).sendPrivateMessage("How are you?", nodes.get(2));

        nodes.get(2).sendPrivateMessage("Hi! Fine and you Node 1?", nodes.get(0));*/
    }
}
