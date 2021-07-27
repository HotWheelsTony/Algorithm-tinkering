import java.util.Arrays;

public class Knapsack01 {

    public static void main(String[] args) {
        new Knapsack01();
    }

    public Knapsack01() {

        //maximum knapsack capacity, arbitrarily set to 10
        int capacity = 10;

        //generate input values
        int[][] generatedElements = generateValues();

        //get the values and weights from generated input
        int[] values = generatedElements[0];
        int[] weights = generatedElements[1];

        //solve this problem
        int solution = solve(capacity, weights, values);

        System.out.println("Maximum value given maximum weight restriction: " + solution);
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
     * @param weights        the weights associated with each value (associated by index)
     * @param values         the (integer) value of each element
     * @return the maximum value this knapsack can hold given it's weight restriction
     */
    public int solve(int capacity, int[] weights, int[] values) {
        //barometer
        int n = 0;

        int[][] DParray = new int[values.length + 1][capacity + 1];

        for (int row = 0; row <= values.length; row++) {
            for (int col = 0; col <= capacity; col++) {

                //Fill row and col 0 with 0's to prevent index out of bounds
                if (row == 0 || col == 0) {
                    DParray[row][col] = 0;
                } else if (weights[row - 1] <= col) {

                    //Value of the new available element
                    int value = values[row - 1];
                    int lookupValue = DParray[row - 1][col - weights[row - 1]];

                    //take the max of the previous max value and the sum value plus lookupvalue
                    DParray[row][col] = Math.max(DParray[row - 1][col], value + lookupValue);

                } else {

                    //else set this value as the value in the row above
                    DParray[row][col] = DParray[row - 1][col];
                }

                //increment barometer
                n++;
            }
        }

        System.out.println("Input values: " + Arrays.toString(values) + "\nInput weights: " + Arrays.toString(weights) + "\n");

        System.out.println("Knapsack weight capacity: " + capacity + "\n");

        //print the DP array
        StringBuilder array = new StringBuilder();
        for (int[] ints : DParray) {
            array.append(Arrays.toString(ints)).append("\n");
        }
        System.out.println(array);

        //print barometer
        int inputSize = capacity * values.length;
        int complexity = (int) (Math.log(n) / Math.log(inputSize));
        System.out.println("\nInput Size (capacity times elements): " + inputSize + ", Steps taken: " + n + ", complexity: O(n^" + complexity + ")\n");

        //return the last value in the DParray as the solution
        return DParray[values.length][capacity];

    }

}

