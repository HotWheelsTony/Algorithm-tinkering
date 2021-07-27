import java.util.Random;

public class  Gerrymandering {

    public static void main (String[] args) {
        new Gerrymandering();
    }

    public Gerrymandering() {
        Precinct[] pa = new Precinct[4];

        //simple test cases
        pa[0] = new Precinct(55, 45);
        pa[1] = new Precinct(43, 57);
        pa[2] = new Precinct(60, 40);
        pa[3] = new Precinct(47, 53);

        //Over write simple test cases with test generator
        pa = generatePrecincts();

        System.out.println(gerrymander(pa));
    }

    /**
     * Generate a random number of precincts (between 4 and 100)
     * with a random number of voters for each party
     * (between 4 and 100)
     */
    public Precinct[] generatePrecincts() {
        Random rand = new Random();
        int numPrecincts = 4; //rand.nextInt(96/2) * 2 + 4;

        //The array of precincts to return
        Precinct[] precincts = new Precinct[numPrecincts];

        //the number of voters in each precinct
        int numVoters = rand.nextInt(96/2) * 2 + 4;

        //For each precinct randomly distribute the voters for each party
        for (int i = 0; i < numPrecincts; i++) {
            int AVoters = rand.nextInt(numVoters);
            int BVoters = numVoters - AVoters;
            Precinct p = new Precinct(AVoters, BVoters);
            precincts[i] = p;
        }

        return precincts;
    }

    /**
     * Simple class to represent a precinct
     * and help make things more readable
     */
    static class Precinct {

        /**
         * Field for storing the number of A
         * and B voters in this precinct
         */
        public int AVoters, BVoters;

        public Precinct(int av, int bv) {
            AVoters = av;
            BVoters = bv;
        }

    }

    /**
     * Determines whether it is possible to gerrymander an array of Precincts
     * @param precincts input array of precincts
     * @return true if possible to gerrymander, false if not
     */
    public boolean gerrymander(Precinct[] precincts) {

        //Barometers
        int m = 0, n = 0;

        //Find total number of party A voters and total number of voters regardless of party
        int numPartyAVoters = 0;
        for (Precinct precinct : precincts) {
            numPartyAVoters += precinct.AVoters;
        }
        //Assume the total number of voters in each precinct is the same as per the brief
        int numVoters = precincts[0].AVoters + precincts[0].BVoters;

        //Get the number of precincts
        int numPrecincts = precincts.length;

        //Create 3D array for storing
        int[][][] lookupTable = new int[numPrecincts][numPrecincts / 2][numPartyAVoters - numVoters / 4 - 1];

        //Set all entries to 1 i.e. True
        for (int i = 0; i < numPrecincts; i++) {
            lookupTable[i][0][0] = 1;
        }

        //Iterate over the lookupTable
        for (int i = 1; i < numPrecincts; i++) {
            //increment n barometer
            n++;
            for (int j = 1; j < numPrecincts / 2; j++) {
                for (int k = 1; k < numPartyAVoters - numVoters / 4 - 1; k++) {

                    //increment m barometer
                    m++;

                    if ((i == 1 || j == 1 || k == precincts[0].AVoters)
                        || (lookupTable[i - 1][j - 1][k - precincts[i].AVoters] == 1)
                        || (lookupTable[i - 1][j][k] == 1)) {

                        //Set this index to true
                        lookupTable[i][j][k] = 1;

                    } else {

                        //Set this index to false
                        lookupTable[i][j][k] = 0;

                    }

                }
            }
        }

        System.out.println("n: " + n + ", m: " + m);
        
        for (int s = numVoters / 4 + 1; s < numPartyAVoters - numVoters / 4 - 1; ++s) {
            if (lookupTable[numPrecincts - 1][numPrecincts / 2 - 1][s] == 1) {
                return true;
            }
        }
        return false;
    }
}