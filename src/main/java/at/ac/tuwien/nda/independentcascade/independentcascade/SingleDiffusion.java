package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;

import java.util.*;

public class SingleDiffusion {

  private final Map<Node, List<Node>> graph;
  private final Activationable activationFunction;
  private final Set<Node> seeds = new HashSet<>();

  public SingleDiffusion(
          final Graph graph, final Activationable activationFunction) {
    this.graph = graph.getRepresentation();
    this.activationFunction = activationFunction;
  }

  public float[] executeKDiffusions(final int k) {
    calculateSeeds();
    final Map<Integer, Integer>[] results = new Map[k];
    final float[] avg = new float[seeds.size()];
    for (int i = 0; i < k; i++) {
      results[i] = singleDiffusion();

      for (int j = 0; j < seeds.size(); j++) {
        avg[j] += results[i].get(j);
      }
    }

    for (int i = 0; i < seeds.size(); i++) {
      avg[i] = avg[i] / k;
    }
    return avg;
  }

  public Map<Integer, Integer> executeSingleDiffusion() {
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

  private Map<Integer, Integer> singleDiffusion() {
    final Set<Node> active = new HashSet<>();
    final Deque<Node> target = new ArrayDeque<>();
    final Map<Integer, Integer> result = new HashMap<>();
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
      result.put(result.size() + 1, active.size());
    }
    return result;
  }
}
