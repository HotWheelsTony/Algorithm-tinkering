import java.util.Arrays;

public class DPKnapsack0N {


    public static void main(String[] args) {
        new DPKnapsack0N();
    }


    public DPKnapsack0N() {

        int[][] generatedValues = generateValues();

        int[] values = generatedValues[0];
        int[] weights = generatedValues[1];
        int capacity = 10;

        System.out.println(solve(capacity, weights, values));
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
            result[1][j] = (int) (Math.random() * 15);
        }

        return result;
    }

    /**
     * Solves a knapsack problem given a knapsack with a weight capacity and an array of values with associated weights
     *
     * @param capacity the maximum weight this knapsack can hold
     * @param weights  the weights associated with each value (associated by index)
     * @param values   the (integer) value of each element
     * @return the maximum value this knapsack can hold given it's weight restriction
     */
    public int solve(int capacity, int[] weights, int[] values) {
        //barometer
        int n = 0;

        //Array for storing overlapping sub-problem solutions
        int[] DParray = new int[capacity + 1];

        for (int i = 0; i <= capacity; i++) {
            for (int j = 0; j < values.length; j++) {

                if (weights[j] > i) {
                    continue;
                }

                int subSolution = DParray[i];
                int lookup = DParray[i - weights[j]];
                int value = values[j];

                DParray[i] = Math.max(subSolution, lookup + value);

                //increment barometer
                n++;

            }
        }

        System.out.println("Input values: " + Arrays.toString(values) + "\nInput weights: " + Arrays.toString(weights) + "\n");

        System.out.println("Knapsack weight capacity: " + capacity + "\n");

        //print the DP array
       System.out.println(Arrays.toString(DParray));

        //print barometer
        int inputSize = capacity * values.length;
        double complexity = (Math.log(n) / Math.log(inputSize));
        System.out.println("\nInput Size (capacity times elements): " + inputSize + ", Steps taken: " + n + ", complexity: O(n^" + complexity + ")\n");

        return DParray[capacity];
    }

}
