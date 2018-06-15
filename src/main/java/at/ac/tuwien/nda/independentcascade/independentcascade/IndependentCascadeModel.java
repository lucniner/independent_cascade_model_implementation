package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;

import java.util.*;

public class IndependentCascadeModel {

  private final Set<Node> activatedNodes = new HashSet<>();
  private final PriorityQueue<Node> heap = new PriorityQueue<>();

  private final Map<Node, List<Node>> graph;
  private final Activationable activationFunction;
  private final int budget;

  private Node lastSeed;
  private Node currentBest;

  public IndependentCascadeModel(
          final Graph graph, final Activationable activationFunction, final int budget) {
    this.graph = graph.getRepresentation();
    this.activationFunction = activationFunction;
    this.budget = budget;
  }

  public Set<Node> calculateActiveNodes() {
    initializeActiveGraphSet();
    activateNodes();
    return activatedNodes;
  }

  private void initializeActiveGraphSet() {
    for (final Map.Entry<Node, List<Node>> entry : graph.entrySet()) {
      final Node node = entry.getKey();
      node.setPrevBest(currentBest);
      node.setFlag(0);
      node.setMarginalGain1(calculateSigma(node));
      node.setMarginalGain2(calculateSigma(node, currentBest));
      heap.add(node);
      if (currentBest == null) {
        currentBest = node;
      } else {
        if (node.getMarginalGain1() > currentBest.getMarginalGain1()) {
          currentBest = node;
        }
      }
    }
  }

  private void activateNodes() {
    while (activatedNodes.size() < budget) {
      final Node u = heap.poll();
      if (u == null) {
        throw new RuntimeException("node is null on heap");
      }
      if (u.getFlag() == activatedNodes.size()) {
        activatedNodes.add(u);
        heap.remove(u);
        lastSeed = u;
        continue;
      } else if (u.getPrevBest().equals(lastSeed)) {
        u.setMarginalGain1(u.getMarginalGain2());
      } else {
        u.setMarginalGain1(calculateMarginalGainBasedOnActiveNodes(u));
        u.setPrevBest(currentBest);
        u.setMarginalGain2(calculateMarginalGainBasedOnActiveNodesAndCurrentBest(u));
      }
      u.setFlag(activatedNodes.size());
      /*
       * todo update curr best
       * currentBest to track the user having the maximum marginal  gain w.r.t. S
       * over all users examined in the current iteration
       */

      final Comparator<Node> cmp = Comparator.comparing(Node::getMarginalGain1, Double::compareTo);

      final Optional<Node> maxValue = this.graph.keySet().stream().max(cmp);
      maxValue.ifPresent(max -> currentBest = max);
      heap.add(u);
    }
  }

  // TODO implement me
  private double calculateSigma(final Node node) {
    return activationFunction.getProbability() * this.graph.get(node).size();
  }

  private double calculateSigma(final Node node, final Node currentBest) {
    int count = 0;
    final List<Node> neighbours = this.graph.get(node);
    for (final Node n : neighbours) {
      if (neighbours.contains(currentBest)) {
        if (activationFunction.getsActivated()) {
          count++;
        }
      }
      count++;
    }
    return count;
  }

  private double calculateMarginalGainBasedOnActiveNodes(final Node node) {
    int count = 0;
    for (final Node n : this.graph.get(node)) {
      if (!activatedNodes.contains(n)) {
        count++;
      }
    }

    return count;
  }

  private double calculateMarginalGainBasedOnActiveNodesAndCurrentBest(final Node node) {
    int count = 0;
    if (!node.equals(currentBest)) {
      for (final Node n : this.graph.get(node)) {
        if (!activatedNodes.contains(n) && !n.equals(currentBest)) {
          count++;
        }
      }
    }
    return count;
  }
}
