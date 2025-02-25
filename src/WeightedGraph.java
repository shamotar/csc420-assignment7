import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class WeightedGraph {
  private Map<String, List<Edge>> adjacencyList;
  private Set<String> vertices;

  public WeightedGraph() {
    this.adjacencyList = new HashMap<>();
    this.vertices = new HashSet<>();
  }

  /**
   * Add a vertex to the graph.
   * @param vertex
   */
  public void addVertex(String vertex) {
    vertices.add(vertex);
    if (!adjacencyList.containsKey(vertex)) {
      adjacencyList.put(vertex, new ArrayList<>());
    }
  }

  /**
   * Add an edge between two vertices with a given weight.
   * @param source
   * @param destination
   * @param weight
   */
  public void addEdge(String source, String destination, double weight) {
    addVertex(source);
    addVertex(destination);
    adjacencyList.get(source).add(new Edge(destination, weight));
  }

  /**
   * Get all vertices in the graph.
   * @return Set of vertices
   */
  public Set<String> getVertices() {
    return vertices;
  }

  /**
   * Find the shortest path between two vertices.
   * Implements Dijkstra's algorithm.
   * @param start
   * @param end
   * @return
   */
  public Path findShortestPath(String start, String end) {
    if (!vertices.contains(start) || !vertices.contains(end)) {
      return null;
    }

    Map<String, Double> distances = new HashMap<>();
    Map<String, String> previousVertices = new HashMap<>();
    PriorityQueue<Node> priorityQueue = new PriorityQueue<>();

    // Initialize distances
    for (String vertex : vertices) {
      distances.put(vertex, Double.POSITIVE_INFINITY);
    }
    distances.put(start, 0.0);
    priorityQueue.add(new Node(start, 0.0));

    while (!priorityQueue.isEmpty()) {
      Node current = priorityQueue.poll();
      String currentVertex = current.vertex;

      if (currentVertex.equals(end)) {
        break;
      }

      if (current.distance > distances.get(currentVertex)) {
        continue;
      }

      List<Edge> neighbors = adjacencyList.get(currentVertex);
      if (neighbors != null) {
        for (Edge edge : neighbors) {
          double distance = distances.get(currentVertex) + edge.weight;

          if (distance < distances.get(edge.destination)) {
            distances.put(edge.destination, distance);
            previousVertices.put(edge.destination, currentVertex);
            priorityQueue.add(new Node(edge.destination, distance));
          }
        }
      }
    }

    // Reconstruct path
    if (distances.get(end) == Double.POSITIVE_INFINITY) {
      return null;
    }

    List<String> path = new ArrayList<>();
    String current = end;
    while (current != null) {
      path.add(0, current);
      current = previousVertices.get(current);
    }

    return new Path(path, distances.get(end));
  }


  /**
   * Represents an edge between two vertices with a given weight.
   * Used in the adjacency list.
   * @see WeightedGraph
   */
  private static class Edge {
    String destination;
    double weight;

    Edge(String destination, double weight) {
      this.destination = destination;
      this.weight = weight;
    }
  }

  /**
   * Represents a vertex with a distance value.
   * Used in the priority queue.
   * @see WeightedGraph
   */
  private static class Node implements Comparable<Node> {
    String vertex;
    double distance;

    Node(String vertex, double distance) {
      this.vertex = vertex;
      this.distance = distance;
    }

    @Override
    public int compareTo(Node other) {
      return Double.compare(this.distance, other.distance);
    }
  }

  /**
   * Represents a path between two vertices with a total distance.
   * @see WeightedGraph
   */
  public static class Path {
    public final List<String> vertices;
    public final double totalDistance;

    public Path(List<String> vertices, double totalDistance) {
      this.vertices = vertices;
      this.totalDistance = totalDistance;
    }
  }
}
