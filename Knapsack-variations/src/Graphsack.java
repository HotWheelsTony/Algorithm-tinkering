import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Graphsack {

    public static void main (String[] args) {
        new Graphsack();
    }

    public Graphsack () {

        int capacity = 100;

        Node startNode = new Node(); //no items, 0 weight 0 value

        solve(capacity, startNode);
        System.out.println("Weight: " + target.getWeight() + ", value: " + target.getValue() + ", steps: " + n);
        System.out.println(target.items);

        //print barometer
        int inputSize = capacity * 3;
        int complexity = (int) (Math.log(n) / Math.log(inputSize));
        System.out.println("\nInput Size (capacity times elements): " + inputSize + ", Steps taken: "
                + n + ", complexity: O(n^" + complexity + ")\n");

    }

    int n = 0;
    Node target = new Node();

    /**
     * Recursive method for finding the maximum value a knapsack can hold given a weight capacity
     *
     * @param capacity the weight capacity of the knapsack
     * @param node the current node being examined
     */
    public void solve (int capacity, Node node) {

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

        public Node (Item... items) {
            this.items.addAll(Arrays.asList(items));
        }

        public Node (List<Item> items) {
            this.items = items;
        }

        public int getWeight () {
            int res = 0;
            for (Item item : items) {
                res += item.getWeight();
            }
            return res;
        }

        public int getValue () {
            int res = 0;
            for (Item item : items) {
                res += item.getValue();
            }
            return res;
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

        public Item (Item.Type t) {
            this.type = t;
        }

        public int getWeight () {
            return this.type.weight;
        }

        public int getValue () {
            return this.type.value;
        }

        @Override
        public String toString() {
            return this.type.toString();
        }
    }

}
