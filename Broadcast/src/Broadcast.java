import java.util.*;

public class Broadcast {

    public static void main(String[] args) {
        new Broadcast();
    }

    public Broadcast() {
        Node I = new Node("I");
        Node F = new Node("F");
        Node C = new Node("C");
        Node G = new Node("G");
        Node H = new Node("H");
        Node D = new Node("D");
        Node E = new Node("E", F);
        Node B = new Node("B", D, E);
        Node A = new Node("A", B, C);
        System.out.println("Minimum number of calls: " + notify(A));
        //Add root node first
        order.add(0, A);
        System.out.println(order);
        System.out.println("Input size: " + order.size() + ", Steps taken: " + n);
    }

    /**
     * Simple class for representing a node and
     * storing a collections of nodes as its children
     */
    static class Node {

        public List<Node> children = new ArrayList<>();

        public String name;

        public Node(String n, Node... c) {
            children.addAll(Arrays.asList(c));
            name = n;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    //list to record the order in which nodes are called
    public List<Node> order = new ArrayList<>();

    //barometer
    int n = 0;

    /**
     * Recursive method for finding the minimum number
     * of calls to notify every node in a tree given the root node.
     * @param node the current node
     * @return an integer describing the minimum number of calls needed to
     * notify(reach) each node in this node's subtree
     */
    public int notify(Node node) {

        //increment barometer
        n++;

        //set the result for minimum number of calls to initially
        // be just the number of children for this node
        int result = node.children.size();

        Map<Integer, Node> sequenceTemp = new HashMap<>();

        //get minimum call values for each child's subtree
        for (Node child : node.children) {
            sequenceTemp.put(notify(child), child);
        }

        List<Integer> minSubCalls = new ArrayList<>(sequenceTemp.keySet());

        //sort calls for children and their subtrees in decreasing order i.e. largest number of calls first
        minSubCalls.sort(Collections.reverseOrder());

        //preserve sequence of calls
        if (!minSubCalls.isEmpty()) {
            for (int i = 0; i < minSubCalls.size(); i++) {
                order.add(i, sequenceTemp.get(minSubCalls.get(i)));
            }
        }

        //check if the number of children this node has does not outweigh the
        // calls needed for any of it's children's subtrees
        for (int i = 0; i < node.children.size(); i++) {
            result = Math.max(result, minSubCalls.get(i) + i + 1);
        }

        //return the minimum number of calls for this subtree
        return result;
    }

}
