import java.util.*;

public class Node {

    public final int id, x, y;

    public Map<Integer, Integer> distances = new HashMap<>();

    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }


    /**
     * Finds the average distance from this node to all
     * other nodes
     *
     * @param nodeSet the set of nodes
     * @return the average distance from this node to all other nodes
     */
    public int averageDist(Collection<Node> nodeSet) {
        int totalDist = 0;

        //find each distance
        for (Node node : nodeSet) {
            if (node == this) continue;

            //find the distance to this node
            int edgeWeight = getDistanceTo(node);

            //also remember the distance to this node
            distances.put(node.id, edgeWeight);

            //add it to the total
            totalDist += edgeWeight;
        }

        //find the average distance
        return totalDist / nodeSet.size() - 1;
    }


    /**
     * Finds the weight or distance of an edge between two nodes
     *
     * @param other the other node
     * @return the distance between them
     */
    public int getDistanceTo(Node other) {
        int xd = Math.abs(this.x - other.x);
        int yd = Math.abs(this.y - other.y);
        return (int) Math.sqrt(xd * xd + yd * yd);
    }


    /**
     * Gets the closest node to this node and removes it
     * from this nodes map of nodes and their distances
     *
     * @return the closest node
     */
    public Node getClosest(Map<Integer, Node> graph) {
        //get the closest node to this node
        Map.Entry<Integer, Integer> min = null;
        for (Map.Entry<Integer, Integer> entry : distances.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }

        assert min != null;
        Node node = graph.get(min.getKey());
        distances.remove(node.id);

        return node;
    }

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }
}



















