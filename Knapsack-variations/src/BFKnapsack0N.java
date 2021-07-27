public class BFKnapsack0N {

    /**
     * This is my attempt at the Brute force 0-N knapsack
     * problems although I could not figure out how to get it working properly
     * @param args
     */

    public static void main (String[] args) {
        new BFKnapsack0N();
    }

    public BFKnapsack0N () {

        int[][] generatedValues = generateValues();

        int[] values = {60, 90, 145};
        int[] weights = {6, 9, 14};
        int capacity = 80;

        solve(capacity, 0, 0, weights, values);
        System.out.println("Maximum value: " + maxValue + ", input size: " + capacity * values.length + ", steps: " + n);
    }

    /**
     * Generates random test inputs for a knapsack problem
     *
     * @return two int arrays, one for values and another for their associated weights
     */
    public int[][] generateValues() {
        //Random number of elements between 0 and 10
        int numElements = (int) (Math.random() * 10);

        //create 2 arrays with n elements
        int[][] result = new int[2][numElements];

        //populate each array
        for (int j = 0; j < numElements; j++) {
            //add a random value for this element between 0 and 150
            result[0][j] = (int) (Math.random() * 150);

            //add a random weight for this element between 0 and 15
            result[1][j] = (int) (Math.random() * 15) + 1;
        }

        return result;
    }

    int maxValue = 0;
    int n = 0;

    public void solve (int capacity, int currentWight, int currentValue, int[] weights, int[] values) {

        if (currentValue > maxValue) {
            maxValue = currentValue;
        }

        //For each item, add one of each item to the current sack
        for (int i = 0; i < values.length; i++) {

            //increment barometer
            n++;

            int nextWeight = currentWight + weights[i];

            if (nextWeight <= capacity) {
                solve(capacity, nextWeight, currentValue + values[i], weights, values);
            }
        }
    }

}
