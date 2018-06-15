package at.ac.tuwien.nda.independentcascade.valueobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {

  private final Map<Node, List<Node>> graph = new HashMap<>();

  public void addNode(int id, int to) {
    if (graph.get(id) == null) {
      List<Node> neighbours = new ArrayList<>();
      neighbours.add(new Node(to));
      graph.put(new Node(id), neighbours);
    } else {
      graph.get(id).add(new Node(to));
    }
  }

  public Map<Node, List<Node>> getRepresentation() {
    return graph;
  }


}
