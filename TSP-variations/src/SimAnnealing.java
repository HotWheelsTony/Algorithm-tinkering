import Common.Node;

import java.io.*;
import java.util.*;


public class SimAnnealing {


    public static final double COOLING_RATE = 0.009;
    public static final int NUM_ITERATIONS = 2000000;
    public static final double TEMP_LIMIT = 0.1;
    public double temperature = 40000;
    public double temp = temperature;


    public static void main(String[] args) {
        String filename = "a280.tsp";
        new SimAnnealing(filename);
    }


    public SimAnnealing(String filename) {
        Map<Integer, Node> graph = parseFile(new File(filename));
        doAnnealing(graph);
    }


    /**
     * Initialise my simulated annealing algorithm,
     * initial sequence, etc.
     */
    public List<Node> createRandomSequence(Map<Integer, Node> graph) {
        //create a random sequence as initial solution
        List<Node> initialSequence = new ArrayList<>(graph.values());

        //shuffle initial sequence
        Collections.shuffle(initialSequence);

        return initialSequence;
    }


    /**
     * Calculates the total distance of a sequence
     *
     * @param sequence
     * @return
     */
    public int getDist(List<Node> sequence) {
        int distance = 0;
        for (int index = 0; index < sequence.size(); index++) {
            Node starting = sequence.get(index);
            Node destination;
            if (index + 1 < sequence.size()) {
                destination = sequence.get(index + 1);
            } else {
                destination = sequence.get(0);
            }
            distance += starting.getDistanceTo(destination);
        }
        return distance;
    }


    /**
     * Swap two random nodes in a given list
     *
     * @param nodes the list of nodes
     */
    public List<Node> swapNodes(List<Node> nodes) {
        //create a copy of this given list to avoid affecting it
        List<Node> copy = new ArrayList<>(nodes);

        //get indexes to swap
        int index1 = (int) (Math.random() * nodes.size());
        int index2 = (int) (Math.random() * nodes.size());

        //swap the nodes
        Collections.swap(copy, index1, index2);

        //return the swapped list
        return copy;
    }

    /**
     * Calculates the acceptance probability
     */
    public boolean acceptable(int neighbourDistance, int bestDistance) {
        if (bestDistance < 5000) {
            int breakpoint = 0;
        }
        //get the difference between the two solutions
        double p = Math.exp(-Math.abs(bestDistance - neighbourDistance) / temp);

        double rand = Math.random();

        return p > rand;
    }

    public boolean criteriaMet(int iteration) {
        return !(iteration < NUM_ITERATIONS
                && temp > TEMP_LIMIT);
    }

    int printy = 0;

    public void doAnnealing(Map<Integer, Node> graph) {
        //create first random solution
        List<Node> currentBestSolution = createRandomSequence(graph);

        //iteration variable
        int iteration = 0;

        //FIXME
        int bestFound = getDist(currentBestSolution);

        while (!criteriaMet(iteration)) {
            //increment iteration
            iteration++;

            //get neighbour solution
            List<Node> neighbourSolution = swapNodes(currentBestSolution);

            //check if the neighbour solution is better
            if (getDist(neighbourSolution) < getDist(currentBestSolution)) {
                //if it is then assign it as current best solution
                currentBestSolution = neighbourSolution;


                //FIXME
                bestFound = getDist(currentBestSolution);
                printy = bestFound;

                //else if see if it's possible to move to this neighbour solution even though it's worse
            } else if (acceptable(getDist(neighbourSolution), getDist(currentBestSolution))) {
                //move to the neighbour solution
                currentBestSolution = neighbourSolution;
            }

            //apply cooling
            temp = temperature / (1 + COOLING_RATE * iteration);

            if (iteration % 28000 == 0) {
                System.out.println("After " + iteration + " iterations: " + bestFound);
            }
        }

        System.out.println("Best solution found: " + bestFound);
        System.out.println("Actual solution found: " + getDist(currentBestSolution));
        System.out.println("Iterations: " + iteration);

    }


    /**
     * Main simulated annealing implementation
     *
     * @param graph the graph of nodes
     */
    public void solve(Map<Integer, Node> graph) {
        int rec = 0;

        //create initial solution
        List<Node> currentSolution = createRandomSequence(graph);

        //set the current solution's distance as the best distance
        List<Node> bestSolution = new ArrayList<>(currentSolution);

        //iterate number of iterations times
        for (int i = 0; i < NUM_ITERATIONS; i++) {

            if (temp > 0.1) {
                rec = i;

                //make a copy of current solution
                List<Node> copySolution = new ArrayList<>(currentSolution);

                //swap two random nodes in the current solution to get a neighbouring solution
                swapNodes(currentSolution);

                //get the total distance of the current and best solutions
                int currentDistance = getDist(currentSolution);
                int bestDistance = getDist(bestSolution);

                //if the neighbour solution is better
                if (currentDistance < bestDistance) {
                    //accept this solution
                    bestSolution = currentSolution;

                    //else determine whether it is acceptable to accept this solution
                } else if (!acceptable(currentDistance, bestDistance)) {
                    //if not then undo swap
                    currentSolution = copySolution;
                }

                //solution is accepted

                //if temperature is too cold, break
            } else {
                break;
            }

            //apply cooling
            //temperature *= COOLING_RATE;
            temp = temperature / (1 + COOLING_RATE * i);
        }

        System.out.println("Solution distance: " + getDist(bestSolution));
        System.out.println("Sequence: ");
        for (Node node : currentSolution) {

        }

        System.out.println("Iterations: " + rec);

    }


    /**
     * Parses a euclidean 2d TSP file into Nodes
     *
     * @param file the file to be passed
     * @return a map of nodes with the corresponding data
     */
    public Map<Integer, Node> parseFile(File file) {
        Map<Integer, Node> nodes = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            //skip first few lines of description
            for (int i = 0; i < 6; ++i) {
                reader.readLine();
            }

            while ((line = reader.readLine()) != null) {
                //if this is the end of the file, break
                if (line.equals("EOF")) {
                    break;
                }

                //trim whitespace and split line into tokens
                String[] data = line.trim().split("\\s+");

                int id = Integer.parseInt(data[0]);
                int x = Integer.parseInt(data[1]);
                int y = Integer.parseInt(data[2]);

                //create a new node with this data
                Node node = new Node(id, x, y);

                //add this new node to the map
                nodes.put(id, node);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error parsing file: " + e.getMessage());
        }

        //return the map of nodes
        return nodes;
    }

}