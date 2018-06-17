package at.ac.tuwien.nda.independentcascade.visualization;

import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import java.util.*;

public class Visualizer {

  private final ProblemGraph problem;
  private final Set<Integer> seeds;
  private final Set<Integer> activatedNodes = new HashSet<>();
  private final Set<Node> seedNodes = new HashSet<>();
  private final Set<Node> activeNodes = new HashSet<>();
  private final Set<Integer> drawnNodes = new HashSet<>();
  private final Graph graph = new MultiGraph("graph");

  public Visualizer(
          final ProblemGraph problem,
          final Set<Integer> seeds,
          final Map<Integer, Set<Integer>> activatedNodes) {
    this.problem = problem;
    this.seeds = seeds;
    activatedNodes.forEach((k, v) -> this.activatedNodes.addAll(v));
  }

  public void createGraph() {

    for (ProblemNode n : problem.getRepresentation().keySet()) {
      final int id = n.getId();
      if (seeds.contains(id)) {
        final Node seedNode = graph.addNode(String.valueOf(id));
        seedNodes.add(seedNode);
        seedNode.addAttribute("ui.style", "fill-color: blue;size: 30px;");
      } else if (activatedNodes.contains(id)) {
        final Node activeNode = graph.addNode(String.valueOf(id));
        activeNodes.add(activeNode);
        activeNode.addAttribute("ui.style", "fill-color: yellow;size: 22px;");
      }
    }

    for (Integer seed : seeds) {
      final Node seedNode = graph.getNode(String.valueOf(seed));
      final List<ProblemNode> neighbours = problem.getRepresentation().get(new ProblemNode(seed));
      drawNeighbours(seedNode, neighbours);
    }

    for (Integer active : activatedNodes) {
      final Node currentActive = graph.getNode(String.valueOf(active));
      final List<ProblemNode> neighbours = problem.getRepresentation().get(new ProblemNode(active));
      if (neighbours == null) {
        continue;
      }
      drawNeighbours(currentActive, neighbours);
    }


    graph.display(true);
  }

  private void drawNeighbours(final Node from, final List<ProblemNode> neighbours) {
    int count = 0;
    for (final ProblemNode n : neighbours) {
      if (activatedNodes.contains(n.getId())) {
        final Node node = graph.getNode(String.valueOf(n.getId()));
        if (from != null && node != null) {
          graph.addEdge(UUID.randomUUID().toString(), from, node);
        }
      } else {
        if (count < 4 && !drawnNodes.contains(n.getId())) {
          drawnNodes.add(n.getId());
          final Node neighbourNode = graph.addNode(String.valueOf(n.getId()));
          seedNodes.add(neighbourNode);
          neighbourNode.addAttribute("ui.style", "fill-color: black;size: 20px;");
          graph.addEdge(UUID.randomUUID().toString(), from, neighbourNode);
          count++;
        }
      }
    }
  }
}
