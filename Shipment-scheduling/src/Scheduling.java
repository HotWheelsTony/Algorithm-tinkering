import java.util.Arrays;

public class Scheduling {

    public static void main(String[] args) {
        new Scheduling();
    }

    public Scheduling() {
        int[] values = new int[10];

        //Example array from handout
        values[0] = 11;
        values[1] = 9;
        values[2] = 9;
        values[3] = 12;
        values[4] = 12;
        values[5] = 12;
        values[6] = 12;
        values[7] = 9;
        values[8] = 9;
        values[9] = 11;

        //generate random test, comment this line out to test array from handout
        //values = generateShipments();

        schedule(values);
    }

    /**
     * Generate a random number of weeks with random
     * values for each weeks shipment
     * @return a new array of integers of length totalWeeks
     */
    public int[] generateShipments() {

        //generate random number of weeks up to 10
        int totalWeeks = (int) (Math.random() * 10);

        //Create array for storing the values for each week
        int[] values = new int[totalWeeks];

        //populate array with random values
        for (int i = 0; i < totalWeeks; i++) {
            values[i] = (int) (Math.random() * 50);
        }

        return values;
    }

    /**
     * Method for finding the optimal (lowest cost) schedule for
     * shipping pc parts according the rates of company A and B
     * @param values an array of (integers) values describing the
     *               total weight of shipments for each week
     */
    public void schedule(int[] values) {
        if(values.length == 0) {
            System.out.println("Input array size 0, must be greater than or equal to 1");
            return;
        }

        //barometer
        int n = 0;

        //Set the multipliers/rates for each company as per the handout
        int r = 1, c = 10;

        //create arrays for storing the schedule and cost
        int[] cost = new int[values.length];
        char[] schedule = new char[values.length];

        //iterate over the array of values for each week
        for (int week = 0; week < values.length; week++) {

            //increment barometer
            n++;

            //reset the rates for each company
            int companyARate = r * values[week];
            int companyBRate = c * 4;

            //if this isn't the first week then add the cost of the prior week to company A's rate
            if (week > 0) {
                companyARate += cost[week - 1];
            }

            //if we are in week 4 or above then add the cost of the week 4 weeks prior to company B's rate
            if (week >= 4) {
                companyBRate += cost[week - 4];
            }

            //Find the minimum of the two costs
            cost[week] = Math.min(companyARate, companyBRate);
        }


        //trace back through the array of costs and determine which company was used for each week
        for (int week = values.length - 1; week >= 0; week--) {

            if (week == values.length - 1) {
                schedule[week] = cost[week] - cost[week - 1] == values[week] ? 'A' : 'B';
                continue;
            }

            if (cost[week] - values[week + 1] == values[week]) {
                schedule[week] = 'A';
            } else {
                schedule[week] = 'B';
            }
        }


        //print the original cost of each week, optimal schedule, and total cost of schedule
        System.out.println("Original cost of each week: " + Arrays.toString(values));
        System.out.println("Cost per week: " + Arrays.toString(cost));
        System.out.println("Optimal schedule: " + Arrays.toString(schedule));
        System.out.println("Total cost: " + cost[cost.length - 1]);

        //compare barometer to input size
        System.out.println("\nInput size (n): " + values.length + ", steps taken to find optimal schedule: n^" + (int) (Math.log(values.length) / Math.log(n)) + " (" + n +")");
    }

}
