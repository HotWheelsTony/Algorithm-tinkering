import java.io.*;
import java.util.*;

public class Approx {

    public static void main (String[] args) {
        //below are the tsp files I used to test my program

//        String filename = "a280.tsp";
//        String solution = "a280.opt.tour.tsp";

//        String filename = "lin105.tsp";
//        String solution = "lin105.opt.tour.tsp";
//
//        String filename = "kroC100.tsp";
//        String solution = "kroC100.opt.tour.tsp";
//
//        String filename = "st70.tsp";
//        String solution = "st70.opt.tour.tsp";
//
//        String filename = "pr76.tsp";
//        String solution = "pr76.opt.tour.tsp";
//
//        String filename = "kroD100.tsp";
//        String solution = "kroD100.opt.tour.tsp";
//
//        String filename = "kroA100.tsp";
//        String solution = "kroA100.opt.tour.tsp";
//
//        String filename = "eil76.tsp";
//        String solution = "eil76.opt.tour.tsp";
//
//        String filename = "eil51.tsp";
//        String solution = "eil51.opt.tour.tsp";
//
        String filename = "eil101.tsp";
        String solution = "eil101.opt.tour.tsp";

        new Approx(filename, solution);
    }


    public Approx (String filename, String solutionFile) {
        List<Integer> optTour = parseSolution(new File(solutionFile));
        File f = new File(filename);
        Map<Integer, Node> nodes = parseFile(f);

        //solve and check against optimal solution
        checkSolution(optTour, nodes);
        solve(nodes);

    }

    /**
     * Solve this TSP problem approximately
     *
     * @param graph the graph of nodes in this problem
     */
    public void solve (Map<Integer, Node> graph) {
        //keep track of which nodes have been visited and total distance
        List<Node> visitedNodes = new ArrayList<>();
        int totalDist = 0;

        //get the root node
        Node root = getStartNode(graph);

        //add root to visited nodes
        visitedNodes.add(root);

        //from root explore each node with the next shortest distance
        //get min from this nodes distances key set, this key will retrieve the closest node

        Node currNode = root;

        //main loop
        while (visitedNodes.size() != graph.keySet().size()) {

            //get the closest node to the current node
            Node closest = currNode.getClosest(graph);

            //skip this node if it is already visited
            if (visitedNodes.contains(closest)) {
                continue;
            }

            //add it to the sequence of visited nodes
            visitedNodes.add(closest);

            //add distance to total distance
            totalDist += currNode.getDistanceTo(closest);

            //set the new node as the current node
            currNode = closest;
        }


        //print total distance for this route
        System.out.println("Total distance of approximate route: " + totalDist + "\n\nSequence of tour: ");

        //print the sequence
        for (Node n : visitedNodes) {
            System.out.println(n);
        }

        //add root node as last node in sequence
        System.out.println(root);
    }



    /**
     * Finds the node to start at, this node being the node
     * with the smallest average distance to every other node
     *
     * @param graph the graph to examine
     * @return the node with the lowest avg distance
     */
    public Node getStartNode (Map<Integer, Node> graph) {
        //start at node with lowest average distance to all other nodes
        Map<Integer, Integer> avgDists = new HashMap<>();

        //put each avg dist in the map with node id as key
        for (Node node : graph.values()) {
            avgDists.put(node.id, node.averageDist(graph.values()));
        }

        //get the lowest entry in avg dists to find start node
        Map.Entry<Integer, Integer> min = null;
        for (Map.Entry<Integer, Integer> entry : avgDists.entrySet()) {
            if (min == null || min.getValue() > entry.getValue()) {
                min = entry;
            }
        }

        //make sure min isn't null
        assert min != null;
        return graph.get(min.getKey());
    }


    /**
     * Parses a euclidean 2d TSP file into Nodes
     *
     * @param file the file to be passed
     * @return a map of nodes with the corresponding data
     */
    public Map<Integer, Node> parseFile (File file) {
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


    /**
     * Check the distance of the optimal tour
     *
     * @param sequence the sequence of nodes in the optimal tour
     * @param graph the graph of nodes
     */
    public void checkSolution (List<Integer> sequence, Map<Integer, Node> graph) {
        int totalDistance = 0;
        Node currNode = graph.get(sequence.get(0));

        for (int i = 1; i < sequence.size(); ++i) {
            int id = sequence.get(i);
            id = Math.abs(id);
            Node next = graph.get(id);
            totalDistance += currNode.getDistanceTo(next);
            currNode = next;
        }

        System.out.println("\nTotal distance of optimal tour: " + totalDistance + "\n");
    }


    /**
     * Parse the solution for a tsp file
     *
     * @param file the file to be parsed
     * @return the sequence of nodes for the optimal tour
     */
    public List<Integer> parseSolution (File file) {
        //create new list to store solution
        List<Integer> optTour = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;

            //skip first few lines of description
            for (int i = 0; i < 5; ++i) {
                reader.readLine();
            }

            while ((line = reader.readLine()) != null) {

                //clean up the bloody tsp file oh my god what even are these things they're horrible
                line = line.trim();
                line = line.replaceAll("\0", "");

                if (line.equals("EOF")) {
                    break;
                }

                //read and add id to opt tour
                optTour.add(Integer.parseInt(line));

                //this line is only needed for a280.tsp because it is a crap file
                //reader.readLine();
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error parsing file: " + e.getMessage());
        }

        return optTour;
    }


}
