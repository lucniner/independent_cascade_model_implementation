package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;

import java.util.*;

public class IndependentCascadeModel {

  private final Set<Node> seeds = new HashSet<>();
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
    int currentActive = 0;
    while (activatedNodes.size() < budget) {
      if (activatedNodes.size() > currentActive) {
        System.out.println("new iteration " + currentActive);
        currentActive = activatedNodes.size();
      }
      final Node u = heap.poll();
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

      final Comparator<Node> cmp = Comparator.comparing(Node::getMarginalGain1, Double::compareTo);
      final Optional<Node> maxValue = this.graph.keySet().stream().max(cmp);
      maxValue.ifPresent(max -> currentBest = max);

      heap.add(u);
    }
  }

  private double calculateSigma(final Node node) {
    if (this.activationFunction.getsActivated()) {
      this.seeds.add(node);
      return countMaxInfluence(node);
    } else {
      return 0;
    }
  }

  private int countMaxInfluence(final Node node) {
    final List<Node> neighbours = this.graph.get(node);
    int activated = 1;
    if (neighbours == null) {
      return 1;
    } else {
      for (final Node n : neighbours) {
        if (!this.seeds.contains(n) && this.activationFunction.getsActivated()) {
          this.seeds.add(node);
          activated += countMaxInfluence(n);
        }
      }
    }
    return activated;
  }

  private double calculateSigma(final Node node, final Node currentBest) {
    if (currentBest == null) {
      return countMaxInfluence(node);
    } else {
      return countMaxInfluence(node) + countMaxInfluence(currentBest);
    }
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
    for (final Node n : this.graph.get(node)) {
      if (!activatedNodes.contains(n) && !n.equals(currentBest)) {
        count++;
      }
    }
    return count++;
  }
}
