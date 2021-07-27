import java.util.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BettererGraphsack {

    public static void main(String[] args) {
        new BettererGraphsack();
    }

    public BettererGraphsack() {

        int capacity = 50;

        Node startNode = new Node(); //no items, 0 weight 0 value

        solve(capacity, startNode);
        System.out.println("Weight: " + target.getWeight() + ", value: " + target.getValue() + ", steps: " + n);
        System.out.println(target.items);

        //print barometer
        int inputSize = capacity * 3; //only testing with 3 available items
        double complexity = Math.log(n) / Math.log(inputSize);
        System.out.println("\nInput Size (capacity times elements): " + inputSize + ", Steps taken: "
                + n + ", complexity: O(n^" + complexity + ")\n");

    }

    int n = 0;
    Node target = new Node();
    //map of visited nodes for pruning
    Map<Node, Boolean> visitedNodes = new HashMap<>();

    /**
     * Recursive method for finding the maximum value a knapsack can hold given a weight capacity
     *
     * @param capacity the weight capacity of the knapsack
     * @param node     the current node being examined
     */
    public void solve(int capacity, Node node) {

        if (!visitedNodes.containsKey(node)) {
            visitedNodes.put(node, true);
        } else {
            return;
        }

        if (node.getValue() > target.getValue()) {
            target = node;
        }

        for (Item.Type type : Item.Type.values()) {
            //increment barometer
            n++;

            //add 1 of each item type for each recursion
            List<Item> items = new ArrayList<>(node.items);

            //add one new item of type to the current nodes items
            items.add(new Item(type));

            Node child = new Node(items);

            //check if next child will not be too heavy
            if (child.getWeight() <= capacity) {
                solve(capacity, child);
            }
        }
    }


    /**
     * Utility classes
     */

    static class Node {

        public List<Item> items = new ArrayList<>();

        public Node(Item... items) {
            this.items.addAll(Arrays.asList(items));
        }

        public Node(List<Item> items) {
            this.items = items;
        }

        public int getWeight() {
            int res = 0;
            for (Item item : items) {
                res += item.getWeight();
            }
            return res;
        }

        public int getValue() {
            int res = 0;
            for (Item item : items) {
                res += item.getValue();
            }
            return res;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Node) {

                Node other = (Node) obj;

                return !this.items.isEmpty() && !other.items.isEmpty() && this.items.size() == other.items.size();
            }
            return false;
        }

        @Override
        public int hashCode() {
            return this.getWeight() * this.getValue();
        }
    }

    static class Item {

        public enum Type {

            SAPPHIRE(6, 60),

            RUBY(9, 90),

            PEARL(14, 145);

            private int weight;
            private int value;

            Type(int w, int v) {
                weight = w;
                value = v;
            }

        }

        public final Type type;

        public Item(Item.Type t) {
            this.type = t;
        }

        public int getWeight() {
            return this.type.weight;
        }

        public int getValue() {
            return this.type.value;
        }

        public int getID () {
            return this.type.ordinal();
        }

        @Override
        public String toString() {
            return this.type.toString();
        }
    }



}



