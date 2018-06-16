package at.ac.tuwien.nda.independentcascade.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;
import util.Pair;

import java.util.*;

public class IndependentCascadeModelAlternative {

    private final Map<Node, List<Node>> graph;
    private final Activationable activationFunction;
    private final int budget;

    private final Map<Pair<Integer, Integer>, Boolean> activationProbabilityEdges  = new HashMap<>();
    private final Set<Node> seedSet = new HashSet<>();
    private final PriorityQueue<Node> queue = new PriorityQueue<>();
    private Node lastSeed = null;
    private Node currBest = null;

    private final HashSet<Integer> alreadyActivated = new HashSet<>();

    public IndependentCascadeModelAlternative(
            final Graph graph, final Activationable activationFunction, final int budget) {
        this.graph = graph.getRepresentation();
        this.activationFunction = activationFunction;
        this.budget = budget;
    }

    public Pair<Integer, Set<Node>> run() {
        init();
        generate_seed();

        return new Pair(alreadyActivated.size(), seedSet);
    }

    private void generate_seed() {
        while (seedSet.size() < budget) {
            Node u = queue.poll();

            if (u.getFlag() == seedSet.size()) {
                seedSet.add(u);

                alreadyActivated.add(u.getId());
                for (Node neighbors : this.graph.get(u)) {
                    if (activationProbabilityEdges.get(new Pair(u.getId(), neighbors.getId()))) {
                        alreadyActivated.add(neighbors.getId());
                    }
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
        for (final Map.Entry<Node, List<Node>> entry : graph.entrySet()) {
            final Node from = entry.getKey();
            final List<Node> toList = entry.getValue();
            for (Node to : toList) {
                activationProbabilityEdges.put(new Pair(from.getId(), to.getId()), this.activationFunction.getsActivated());
            }
        }

        for (final Map.Entry<Node, List<Node>> entry : graph.entrySet()) {
            final Node u = entry.getKey();
            u.setFlag(0);
            u.setPrevBest(currBest);
            u.setMarginalGain1(sigma(u, alreadyActivated));
            u.setMarginalGain2(sigma(u, currBest, alreadyActivated));
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

    private double sigma(final Node node, final HashSet<Integer> alreadyActivated) {
        final List<Node> neighbors = this.graph.get(node);
        int activated = 1;

        if (neighbors == null) {
            return activated;
        } else {
            for (final Node n : neighbors) {
                if (!alreadyActivated.contains(n.getId())) {
                    if (activationProbabilityEdges.get(new Pair(node.getId(), n.getId()))) {
                        activated += 1;
                    }
                }
            }
        }

        return activated;
    }

    private double sigma(final Node node, final Node cureBest, final HashSet<Integer> alreadyActivated) {
        if (cureBest == null) {
            return sigma(node, alreadyActivated);
        } else {
            HashSet<Integer> set = new HashSet<>(alreadyActivated);
            final List<Node> cureBestNeighbors = this.graph.get(cureBest);

            for (final Node n : cureBestNeighbors) {
                if (!set.contains(n.getId())) {
                    if (activationProbabilityEdges.get(new Pair(cureBest.getId(), n.getId()))) {
                        set.add(n.getId());
                    }
                }
            }
            return sigma(node, set);
        }
    }
}
