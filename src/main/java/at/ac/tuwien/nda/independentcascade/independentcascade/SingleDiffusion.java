package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;

import java.util.*;

public class SingleDiffusion {

  private final Map<Node, List<Node>> graph;
  private final Activationable activationFunction;
  private final Set<Node> seeds = new HashSet<>();

  public SingleDiffusion(final Graph graph, final Activationable activationFunction) {
    this.graph = graph.getRepresentation();
    this.activationFunction = activationFunction;
  }

  public Map<Node, Float> executeKDiffusions(final int k) {
    calculateSeeds();
    final Map<Node, Integer>[] results = new Map[k];
    final Map<Node, Float> avg = new HashMap<>();
    for (int i = 0; i < k; i++) {
      final Map<Node, Integer> result = singleDiffusion();
      for (final Node n : seeds) {
        final Float activatedNodes = result.get(n) * 1F;
        final Float currentActivated = avg.get(n);
        if (currentActivated == null) {
          avg.put(n, activatedNodes);
        } else {
          final Float totalActivated = currentActivated + activatedNodes;
          avg.put(n, totalActivated);
        }
      }
    }

    for (final Node n : seeds) {
      final float currentAverage = avg.get(n);
      avg.put(n, currentAverage / k);
    }
    return avg;
  }

  public Map<Node, Integer> executeSingleDiffusion() {
    calculateSeeds();
    return singleDiffusion();
  }

  private void calculateSeeds() {
    for (final Node node : graph.keySet()) {
      if (activationFunction.getsActivated()) {
        seeds.add(node);
      }
    }
  }

  private Map<Node, Integer> singleDiffusion() {
    final Set<Node> active = new HashSet<>();
    final Deque<Node> target = new ArrayDeque<>();
    final Map<Node, Integer> result = new HashMap<>();
    for (Node s : seeds) {
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
    return result;
  }
}
