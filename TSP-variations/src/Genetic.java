import Common.Node;
import Common.Parser;

import java.util.*;

public class Genetic {

    public static final int MAX_GENERATIONS = 1000;
    public static final int POPULATION_SIZE = 250;
    public static final double MUTATION_RATE = 0.06;
    public static final double CROSSOVER_RATE = 0.99;
    public static final int ELITISM = 3;
    public static final int TOURNAMENT_SIZE = 10;
    public int currentGen = 1;

    public static void main(String[] args) {
        new Genetic();
    }

    public Genetic() {
        Map<Integer, Node> graph = Parser.parseFile("a280.tsp");

        solve(graph);
    }


    /**
     * Use a genetic algorithm to solve a TSP problem
     *
     * @param graph
     */
    public void solve(Map<Integer, Node> graph) {
        //create population by creating a set of solutions (lists of nodes)
        Population population = new Population(POPULATION_SIZE, graph);

        //get initial solution
        List<Node> initialSolution = population.solutions.get(0);

        System.out.println("Initial fittest solution distance: ");

        //iterate until stop criteria is met
        while (currentGen <= MAX_GENERATIONS) {

            //get sequence from population with the shortest distance at this generation
            List<Node> fittest = population.solutions.get(0);
            System.out.println("Fittest solution at generation " + currentGen + ": " + getDist(fittest));


            //crossover population
            crossover(population);

            //mutate population
            mutate(population);

            //increment generation
            currentGen++;
        }


        //print results
        System.out.println("\nBest solution found: " + getDist(population.solutions.get(0)));
    }


    /**
     * Mutates the given population
     */
    public void mutate(Population population) {
        for (int i = 0; i < population.solutions.size(); ++i) {

            List<Node> solution = population.solutions.get(0);

            if (i >= ELITISM) {
                //randomly determine whether to mutate this solution
                if (Math.random() < MUTATION_RATE) {
                    //swap to random nodes in this solution's sequence
                    swapNodes(solution);
                    population.sortSolutions();
                }
            }
        }
    }


    public void crossover(Population population) {

        for (int i = 0; i < population.solutions.size(); ++i) {

            //get first parent
            List<Node> parent1 = population.solutions.get(0);

            //determine whether to apply crossover to this solution
            if (Math.random() < CROSSOVER_RATE && i >= ELITISM) {
                //get second parent
                List<Node> parent2 = getParent(population);

                //create child
                List<Node> child = new ArrayList<>();

                //create a new solution with half-and-half of both parents sequences
                int splitIndex = (int) (Math.random() * parent1.size());

                for (int j = 0; j < parent1.size(); ++j) {
                    Node node;
                    if (j < splitIndex) {
                        //add parent1 data
                        node = parent1.get(j);
                    } else {
                        //add parent2 data
                        node = parent2.get(j);
                    }
                    //add data to the child
                    if (!child.contains(node)) {
                        child.add(node);
                    }
                }

                //if it's still too small
                if (child.size() != parent1.size()) {
                    for (Node node : parent2) {
                        if (!child.contains(node) && child.size() < parent1.size()) {
                            child.add(node);
                        }
                    }
                }

                if (child.size() == parent1.size()) {
                    //replace parent1 with this child
                    population.solutions.set(i, child);
                    population.sortSolutions();
                }

            }
            //keep parent1, nothing happens
        }
    }


    /**
     * Selects a parent
     *
     * @param population
     * @return
     */
    public List<Node> getParent(Population population) {
        Population res = new Population();

        List<List<Node>> tournament = new ArrayList<>();

        //add random solutions to this tournament
        for (int i = 0; i < TOURNAMENT_SIZE; ++i) {
            int index = (int) (Math.random() * population.solutions.size());
            tournament.add(population.solutions.get(index));
        }
        res.solutions = tournament;
        res.sortSolutions();

        //get the fittest solution from this tournament
        return res.solutions.get(0);
    }


    /**
     * Swap two random nodes in a given list
     *
     * @param nodes the list of nodes
     */
    public void swapNodes(List<Node> nodes) {
        //get indexes to swap
        int index1 = (int) (Math.random() * nodes.size());
        int index2 = (int) (Math.random() * nodes.size());

        //swap the nodes
        Collections.swap(nodes, index1, index2);
    }


    /**
     * Calculates the fitness of a solution
     */
    public double getFitness(List<Node> sequence) {
        return 1 / getDist(sequence);
    }


    /**
     * Calculates the total distance of a sequence
     */
    public double getDist(List<Node> sequence) {
        double distance = 0;
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
     * Class for storing a collection of solutions or "individuals"
     */
    class Population {

        public List<List<Node>> solutions = new ArrayList<>();

        public Population() {
        }

        public Population(int popSize, Map<Integer, Node> graph) {
            //create a new population with popSize number of solutions
            for (int i = 0; i < popSize; ++i) {
                //create and add a random solution to the population
                solutions.add(createRandomSequence(graph));
            }
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

        public void sortSolutions() {
            this.solutions.sort((o1, o2) -> {
                if (getFitness(o1) > getFitness(o2)) {
                    return -1;
                } else if (getFitness(o1) < getFitness(o2)) {
                    return 1;
                }
                return 0;
            });
        }


        /**
         * Calculates the fitness of a solution
         */
        private double getFitness(List<Node> sequence) {
            double distance = 0;
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
            return 1 / distance;
        }
    }

}
