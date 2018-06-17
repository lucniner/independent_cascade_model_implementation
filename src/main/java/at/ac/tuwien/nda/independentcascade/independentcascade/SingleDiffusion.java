package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;

import java.util.*;

/**
 * this class was only an alternative for the other implementation to have some point of reference
 * it is heavily based on Suman Kundu's implementation
 * http://www.sumankundu.info/articles/detail/How-To-Code-Independent-Cascade-Model-of-Information-Diffusion
 */
public class SingleDiffusion {

  private final Map<ProblemNode, List<ProblemNode>> graph;
  private final Activationable activationFunction;
  private final Set<ProblemNode> seeds = new HashSet<>();

  public SingleDiffusion(final ProblemGraph problemGraph, final Activationable activationFunction) {
    this.graph = problemGraph.getRepresentation();
    this.activationFunction = activationFunction;
  }

  public Map<ProblemNode, Float> executeKDiffusions(final int k) {
    calculateSeeds();
    final Map<ProblemNode, Integer>[] results = new Map[k];
    final Map<ProblemNode, Float> avg = new HashMap<>();
    for (int i = 0; i < k; i++) {
      final Map<ProblemNode, Integer> result = singleDiffusion();
      for (final ProblemNode n : seeds) {
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

    for (final ProblemNode n : seeds) {
      final float currentAverage = avg.get(n);
      avg.put(n, currentAverage / k);
    }
    return avg;
  }

  public Map<ProblemNode, Integer> executeSingleDiffusion() {
    calculateSeeds();
    return singleDiffusion();
  }

  private void calculateSeeds() {
    for (final ProblemNode problemNode : graph.keySet()) {
      if (activationFunction.getsActivated()) {
        seeds.add(problemNode);
      }
    }
  }

  private Map<ProblemNode, Integer> singleDiffusion() {
    final Set<ProblemNode> active = new HashSet<>();
    final Deque<ProblemNode> target = new ArrayDeque<>();
    final Map<ProblemNode, Integer> result = new HashMap<>();
    for (ProblemNode s : seeds) {
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
    return result;
  }
}
