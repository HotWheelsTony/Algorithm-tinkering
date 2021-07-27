import java.util.ArrayList;
import java.util.List;

public class SkylineDivCon {

    public static void main(String[] args) {
        new SkylineDivCon();
    }

    public SkylineDivCon() {
        initialize();
        generateBuilding();
    }

    /**
     * Generates a random building between
     * 0 <= x <= 100 and 0 <= y <= 50
     */
    public Building generateBuilding() {
        float x1 = (float) Math.random() * 100;
        float x2 = x1 + (float) Math.random() * (100 - x1);
        float y = (float) Math.random() * 50;

        return new Building(x1, x2, y);
    }

    //barometers
    int divide = 0, combine = 0;

    //Size of input array
    int testSize = 10;

    /**
     * Construct building objects out of input data
     * for the sake of understandability
     */
    public void initialize() {

        if (testSize <= 0) {
            System.out.println("Test size must be larger than zero.");
            return;
        }

        Building[] buildings = new Building[testSize];

        //construct input data as building objects
        for (int i = 0; i < buildings.length; i++) {
            buildings[i] = generateBuilding();
        }

        //sort using adapted merge sort
        List<Skyline> keyPoints = getSkyline(buildings, 0, buildings.length - 1);

        //print the unsorted array
        printArray(buildings);

        //print the sorted list of key points
        System.out.println("Key points of skyline:" + keyPoints);

        //print barometers
        System.out.println("\nInput size: " + testSize + "\nOutput size: " + keyPoints.size() + "\nTotal divisions: " + divide + "\nTotal combines: " + combine);
    }

    /**
     * Recursively creates skyline objects from building objects
     * skyline objects represent key points of building objects
     *
     * @param buildings
     * @param left
     * @param right
     */
    public List<Skyline> getSkyline(Building[] buildings, int left, int right) {

        //increment the divide barometer every time this method is called
        divide++;

        //if there is a single building add both of it's key points as a skyline object to a new list
        if (left == right) {
            //result list to return
            List<Skyline> skyline = new ArrayList<>();

            //add the key points for this building to the result
            skyline.add(new Skyline(buildings[left].start, buildings[left].height));

            skyline.add(new Skyline(buildings[left].end, 0));

            return skyline;
        }

        //find the middle index
        int middle = (left + right) / 2;

        //recursively divide the array of buildings
        List<Skyline> s1 = getSkyline(buildings, left, middle);
        List<Skyline> s2 = getSkyline(buildings, middle + 1, right);

        return mergeSkylines(s1, s2);
    }

    /**
     * Merges two skylines together
     *
     * @param skyline1
     * @param skyline2
     * @return
     */
    public List<Skyline> mergeSkylines(List<Skyline> skyline1, List<Skyline> skyline2) {

        //increment the combine barometer every time this method is called
        combine++;

        //initialize result set
        List<Skyline> result = new ArrayList<>();

        //variables for storing the current tallest point, current height of list 1 and list 2, and the current X position
        float maxHeight = 0, currHeight1 = 0, currHeight2 = 0, currXPos;

        //while both skyline lists have objects to be merged
        while (!skyline1.isEmpty() && !skyline2.isEmpty()) {


            //check which skyline object starts first (has a lower x coordinate)
            if (skyline1.get(0).x < skyline2.get(0).x) {

                //set the current x position to that of the left most building
                currXPos = skyline1.get(0).x;
                //set the first current height to be that of the left most building
                currHeight1 = skyline1.get(0).y;

                //if the current height of list 1 is smaller than the current height of list 2
                if (currHeight1 < currHeight2) {

                    //remove this element
                    skyline1.remove(0);

                    //check the last building added from list 2 set the current max height
                    if (maxHeight != currHeight2 && currHeight1 != currHeight2) {
                        //if it did not then the skyline has changed at this x position and a new key point is constructed
                        result.add(new Skyline(currXPos, currHeight2));
                    }
                } else {
                    //set max height as this height
                    maxHeight = currHeight1;
                    //remove this building
                    skyline1.remove(0);
                    //construct a new key point at this x position and height
                    result.add(new Skyline(currXPos, currHeight1));
                }


            } else {
                //following else block is the same as the above if block but for list 2
                currXPos = skyline2.get(0).x;
                currHeight2 = skyline2.get(0).y;


                if (currHeight2 < currHeight1) {

                    skyline2.remove(0);
                    if (maxHeight != currHeight1 && currHeight2 == currHeight1) {
                        result.add(new Skyline(currXPos, currHeight1));
                    }
                } else {
                    maxHeight = currHeight2;
                    skyline2.remove(0);
                    result.add(new Skyline(currXPos, currHeight2));
                }

            }


        }

        //add any remaining elements
        while (!skyline1.isEmpty()) {
            result.add(skyline1.get(0));
            skyline1.remove(0);
        }

        while (!skyline2.isEmpty()) {
            result.add(skyline2.get(0));
            skyline2.remove(0);
        }

        return result;
    }

    /**
     * Helper method for printing an array
     */
    public void printArray(Building[] input) {
        System.out.println("Input data:");
        for (Building b : input) {
            System.out.println(b.toString());
        }
        System.out.println();
    }

    /**
     * Simple class to represent a building
     * essentially just a triple ({x1, x2, y})
     */
    private static class Building {

        public float start, end, height;

        public Building(float x1i, float x2i, float y) {
            start = x1i;
            end = x2i;
            height = y;
        }

        @Override
        public String toString() {
            return "(" + this.start + ", " + this.end + ", " + this.height + ") ";
        }
    }

    /**
     * Simple class to represent the key points of a
     * piece of the skyline
     */
    private static class Skyline {

        public float x, y;

        public Skyline(float xi, float yi) {
            x = xi;
            y = yi;
        }

        @Override
        public String toString() {
            return "\n(" + this.x + ", " + this.y + ")";
        }
    }

}
