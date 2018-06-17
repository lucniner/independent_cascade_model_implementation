package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;
import util.Pair;

import java.util.*;

/**
 * independent cascade model with the speedup of
 * Goyal et al. from 2011 (CELF++)
 * calculating all possible active nodes before the actual simulation
 */
public class IndependentCascadeModel {

  private final Map<ProblemNode, List<ProblemNode>> graph;
  private final Activationable activationFunction;
  private final int budget;
  private final int scenarioNumber;

  private final Map<Pair<Integer, Integer>, List<Boolean>> activationProbabilityEdges = new HashMap<>();
  private final Set<ProblemNode> seedSet = new HashSet<>();
  private final PriorityQueue<ProblemNode> queue = new PriorityQueue<>();
  private ProblemNode lastSeed = null;
  private ProblemNode currBest = null;

  private final Map<Integer, Set<Integer>> alreadyActivated = new HashMap<>();

  public IndependentCascadeModel(
          final ProblemGraph problemGraph, final Activationable activationFunction, final int budget, final int scenarioNumber) {
    this.graph = problemGraph.getRepresentation();
    this.activationFunction = activationFunction;
    this.budget = budget;
    this.scenarioNumber = scenarioNumber;
  }

  public Pair<Integer, Set<ProblemNode>> run() {
    init();
    generate_seed();

    int sum = 0;
    for (Map.Entry entry : alreadyActivated.entrySet()) {
      sum += ((Set<Integer>) entry.getValue()).size();
    }
    sum = sum / scenarioNumber;
    return new Pair(sum, seedSet);
  }

  private void addAlreadyActivated(int index, ProblemNode u) {
    alreadyActivated.get(index).add(u.getId());
    if (this.graph.containsKey(u)) {
      for (ProblemNode neighbors : this.graph.get(u)) {
        if (!alreadyActivated.get(index).contains(neighbors.getId())) {
          if (activationProbabilityEdges.get(new Pair(u.getId(), neighbors.getId())).get(index)) {
            alreadyActivated.get(index).add(neighbors.getId());
            addAlreadyActivated(index, neighbors);
          }
        }
      }
    }
  }

  private void generate_seed() {
    while (seedSet.size() < budget) {
      ProblemNode u = queue.poll();

      if (u.getFlag() == seedSet.size()) {
        seedSet.add(u);
        for (int i = 0; i < scenarioNumber; i++) {
          addAlreadyActivated(i, u);
        }
        lastSeed = u;
        continue;
      } else if (u.getPrevBest() == lastSeed) {
        u.setMarginalGain1(u.getMarginalGain2());
      } else {
        u.setMarginalGain1(sigma(u, alreadyActivated));
        u.setPrevBest(currBest);
        u.setMarginalGain2(sigma(u, currBest, alreadyActivated));
      }

      u.setFlag(seedSet.size());
      currBest = queue.peek();
      queue.add(u);
    }
  }

  private void init() {
    for (final Map.Entry<ProblemNode, List<ProblemNode>> entry : graph.entrySet()) {
      final ProblemNode from = entry.getKey();
      final List<ProblemNode> toList = entry.getValue();
      for (ProblemNode to : toList) {
        List<Boolean> activatedList = new ArrayList<>(scenarioNumber);

        for (int i = 0; i < scenarioNumber; i++) {
          activatedList.add(this.activationFunction.getsActivated());
        }
        activationProbabilityEdges.put(new Pair(from.getId(), to.getId()), activatedList);
      }
    }

    for (int i = 0; i < scenarioNumber; i++) {
      alreadyActivated.put(i, new HashSet<>());
    }

    for (final Map.Entry<ProblemNode, List<ProblemNode>> entry : graph.entrySet()) {
      final ProblemNode u = entry.getKey();
      u.setFlag(0);
      u.setPrevBest(currBest);
      u.setMarginalGain1(sigma(u, alreadyActivated));
      u.setMarginalGain2(sigma(currBest, alreadyActivated) + sigma(u, currBest, alreadyActivated));
      queue.add(u);

      if (currBest == null) {
        currBest = u;
      } else {
        if (u.getMarginalGain1() > currBest.getMarginalGain1()) {
          currBest = u;
        }
      }
    }
  }

  private double sigma(final ProblemNode problemNode, final Map<Integer, Set<Integer>> alreadyActivated) {
    double influence = 0;
    for (int i = 0; i < scenarioNumber; i++) {
      HashSet<Integer> newSet = new HashSet<>(alreadyActivated.get(i));
      influence += calculateInfluence(i, problemNode, newSet);
    }
    return influence / scenarioNumber;
  }

  private double calculateInfluence(final int index, final ProblemNode problemNode, final HashSet<Integer> alreadyActivated) {
    final List<ProblemNode> neighbors = this.graph.get(problemNode);

    int activated = 1;

    if (neighbors == null) {
      return activated;
    } else {
      alreadyActivated.add(problemNode.getId());
      for (final ProblemNode n : neighbors) {
        if (!alreadyActivated.contains(n.getId())) {
          if (activationProbabilityEdges.get(new Pair(problemNode.getId(), n.getId())).get(index)) {
            activated += calculateInfluence(index, n, alreadyActivated);
          }
        }
      }
    }
    return activated;
  }

  private double sigma(final ProblemNode problemNode, final ProblemNode cureBest, final Map<Integer, Set<Integer>> alreadyActivated) {
    if (cureBest != null) {
      for (int i = 0; i < scenarioNumber; i++) {
        HashSet<Integer> newSet = new HashSet<>(alreadyActivated.get(i));
        calculateInfluence(i, cureBest, newSet);

      }
    }
    return sigma(problemNode, alreadyActivated);
  }

  public Map<Integer, Set<Integer>> getAlreadyActivated() {
    return alreadyActivated;
  }
}
