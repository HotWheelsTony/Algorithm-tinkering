import Common.Node;
import Common.Parser;
import java.util.*;


public class SimulatedAnnealing {


    public static final double COOLING_RATE = 0.009;
    public static final int NUM_ITERATIONS = 2000000;
    public static final double TEMP_LIMIT = 0.1;
    public double temperature = 40000;
    public double temp = temperature;


    public static void main(String[] args) {
        new SimulatedAnnealing();
    }


    public SimulatedAnnealing() {
        Map<Integer, Node> graph = Parser.parseFile("a280.tsp");
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


}