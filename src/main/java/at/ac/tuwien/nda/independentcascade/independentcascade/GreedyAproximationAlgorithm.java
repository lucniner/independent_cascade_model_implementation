package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;

import java.util.*;

/**
 * a greedy implementation based on the lecture slides
 * running for each node all simulations
 */
public class GreedyAproximationAlgorithm {

  private final Map<ProblemNode, List<ProblemNode>> graph;
  private final Activationable activationFunction;
  private final Set<ProblemNode>[] seeds;
  private final int numberOfSimulations;
  private final Set<ProblemNode> activatedProblemNodes = new HashSet<>();

  public GreedyAproximationAlgorithm(
          final ProblemGraph problemGraph,
          final Activationable activationFunction,
          final int numberOfSimulations) {
    this.graph = problemGraph.getRepresentation();
    this.activationFunction = activationFunction;
    this.seeds = new HashSet[numberOfSimulations];
    this.numberOfSimulations = numberOfSimulations;
  }

  public Set<ProblemNode> calculateActivatedNodes() {
    calculateScenarios();
    simulate();
    return activatedProblemNodes;
  }

  private void calculateScenarios() {
    for (int i = 0; i < numberOfSimulations; i++) {
      calculateSeedsForCurrentScenario(i);
    }
  }

  private void calculateSeedsForCurrentScenario(int currentRun) {
    seeds[currentRun] = new HashSet<>();
    for (final ProblemNode problemNode : graph.keySet()) {
      if (activationFunction.getsActivated()) {
        seeds[currentRun].add(problemNode);
      }
    }
  }

  private void simulate() {
    while (activatedProblemNodes.size() < numberOfSimulations) {
      for (final ProblemNode n : graph.keySet()) {
        if (!activatedProblemNodes.contains(n)) {
          final Set<ProblemNode> active = new HashSet<>();
          final Deque<ProblemNode> target = new ArrayDeque<>();
          final Map<ProblemNode, Integer> result = new HashMap<>();
          for (int i = 0; i < numberOfSimulations; i++) {
            for (ProblemNode s : seeds[i]) {
              target.push(s);
              while (!target.isEmpty()) {
                final ProblemNode problemNode = target.pop();
                active.add(problemNode);
                for (ProblemNode follower : graph.get(s)) {
                  if (activationFunction.getsActivated()) {
                    if (!active.contains(follower)) {
                      target.push(follower);
                    }
                  }
                }
              }
              result.put(s, active.size());
            }
            final Comparator<Map.Entry<ProblemNode, Integer>> cmp =
                    Comparator.comparing(Map.Entry::getValue, Integer::compareTo);
            final Optional<Map.Entry<ProblemNode, Integer>> maxValue = result.entrySet().stream().max(cmp);
            maxValue.ifPresent(max -> activatedProblemNodes.add(max.getKey()));
          }
        }
      }
    }
  }
}
