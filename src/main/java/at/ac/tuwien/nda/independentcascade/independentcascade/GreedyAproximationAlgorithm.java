package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;

import java.util.*;

public class GreedyAproximationAlgorithm {

  private final Map<Node, List<Node>> graph;
  private final Activationable activationFunction;
  private final Set<Node>[] seeds;
  private final int numberOfSimulations;
  private final Set<Node> activatedNodes = new HashSet<>();

  public GreedyAproximationAlgorithm(
          final Graph graph,
          final Activationable activationFunction,
          final int numberOfSimulations) {
    this.graph = graph.getRepresentation();
    this.activationFunction = activationFunction;
    this.seeds = new HashSet[numberOfSimulations];
    this.numberOfSimulations = numberOfSimulations;
  }

  public Set<Node> calculateActivatedNodes() {
    calculateScenarios();
    simulate();
    return activatedNodes;
  }

  private void calculateScenarios() {
    for (int i = 0; i < numberOfSimulations; i++) {
      calculateSeedsForCurrentScenario(i);
    }
  }

  private void calculateSeedsForCurrentScenario(int currentRun) {
    seeds[currentRun] = new HashSet<>();
    for (final Node node : graph.keySet()) {
      if (activationFunction.getsActivated()) {
        seeds[currentRun].add(node);
      }
    }
  }

  private void simulate() {
    while (activatedNodes.size() < numberOfSimulations) {
      for (final Node n : graph.keySet()) {
        if (!activatedNodes.contains(n)) {
          final Set<Node> active = new HashSet<>();
          final Deque<Node> target = new ArrayDeque<>();
          final Map<Node, Integer> result = new HashMap<>();
          for (int i = 0; i < numberOfSimulations; i++) {
            for (Node s : seeds[i]) {
              target.push(s);
              while (!target.isEmpty()) {
                final Node node = target.pop();
                active.add(node);
                for (Node follower : graph.get(s)) {
                  if (activationFunction.getsActivated()) {
                    if (!active.contains(follower)) {
                      target.push(follower);
                    }
                  }
                }
              }
              result.put(s, active.size());
            }
            final Comparator<Map.Entry<Node, Integer>> cmp =
                    Comparator.comparing(Map.Entry::getValue, Integer::compareTo);
            final Optional<Map.Entry<Node, Integer>> maxValue = result.entrySet().stream().max(cmp);
            maxValue.ifPresent(max -> activatedNodes.add(max.getKey()));
          }
        }
      }
    }
  }
}
