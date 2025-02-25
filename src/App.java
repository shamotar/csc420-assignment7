/*
 *  Title: Assignment 7 - Weighted Graphs
 *  Created by: Robert Norlander
 *  Email: shamotar@csp.edu
 *  Date: 2025-02-26
 *  Class: CSC 420 - Data Structures and Algorithms
 *  Professor: Susan Furtney
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class App {
    private static final String CITIES_FILE = "src/static/ticket_to_ride.txt";

    public static void main(String[] args) throws Exception {
        System.out.println("Created and submitted by: Robert Norlander - shamotar@csp.edu");
        System.out.println("I certify that this is my own work\n");

        try {
            // Read and build the graph
            WeightedGraph graph = readGraphFromFile();
            Map<Integer, String> cityMap = createCityMap(graph.getVertices());

            // Display cities
            System.out.println("Please select a Starting and Destination city: <Enter the city number>");
            for (Map.Entry<Integer, String> entry : cityMap.entrySet()) {
                System.out.printf("%d > %s%n", entry.getKey(), entry.getValue());
            }

            // Get user input
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nPlease enter starting city:\n");
            int startIdx = scanner.nextInt();
            System.out.print("Please enter destination city:\n");
            int endIdx = scanner.nextInt();

            // Validate input
            if (!cityMap.containsKey(startIdx) || !cityMap.containsKey(endIdx)) {
                System.out.println("Invalid city number selected");
                scanner.close();
                return;
            }

            String startCity = cityMap.get(startIdx);
            String endCity = cityMap.get(endIdx);

            // Find shortest path
            WeightedGraph.Path path = graph.findShortestPath(startCity, endCity);

            if (path != null) {
                System.out.printf("%nThe shortest route from %s to %s: %s (cost %.1f)%n",
                    startCity, endCity, String.join(" >> ", path.vertices), path.totalDistance);
            } else {
                System.out.printf("%nNo route found between %s and %s%n", startCity, endCity);
            }

            // Close scanner
            scanner.close();

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (InputMismatchException e) {
            System.out.println("Please enter valid numeric city numbers");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    /**
     * Read the graph from the file.
     * @return WeightedGraph
     * @throws IOException
     */
    private static WeightedGraph readGraphFromFile() throws IOException {
        WeightedGraph graph = new WeightedGraph();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(CITIES_FILE))) {
            // Read cities from first line
            String citiesLine = reader.readLine();
            String[] cities = citiesLine.split(",");
            for (String city : cities) {
                graph.addVertex(city.trim());
            }

            // Read edges
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String source = parts[0].trim();
                    String destination = parts[1].trim();
                    double weight = Double.parseDouble(parts[2].trim());
                    graph.addEdge(source, destination, weight);
                }
            }
        }
        
        return graph;
    }

    /**
     * Create a map of city index to city name.
     * @param cities
     * @return
     */
    private static Map<Integer, String> createCityMap(Set<String> cities) {
        Map<Integer, String> cityMap = new HashMap<>();
        int index = 0;
        for (String city : cities) {
            cityMap.put(index++, city);
        }
        return cityMap;
    }
}
