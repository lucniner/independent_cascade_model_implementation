package at.ac.tuwien.nda.independentcascade.valueobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProblemGraph {

  private final Map<ProblemNode, List<ProblemNode>> graph = new HashMap<>();

  public void addNode(int id, int to) {
    if (graph.get(new ProblemNode(id)) == null) {
      List<ProblemNode> neighbours = new ArrayList<>();
      neighbours.add(new ProblemNode(to));
      graph.put(new ProblemNode(id), neighbours);
    } else {
      graph.get(new ProblemNode(id)).add(new ProblemNode(to));
    }
  }

  public Map<ProblemNode, List<ProblemNode>> getRepresentation() {
    return graph;
  }


}
