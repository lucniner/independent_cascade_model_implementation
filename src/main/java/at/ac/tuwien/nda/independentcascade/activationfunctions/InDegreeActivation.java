package at.ac.tuwien.nda.independentcascade.activationfunctions;

import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InDegreeActivation implements Activationable {

  private final Random random = new Random();
  private final Map<ProblemNode, Integer> inDegreeMap = new HashMap<>();

  public InDegreeActivation(final ProblemGraph problemGraph) {
    this.calculateInDegrees(problemGraph);
  }

  private void calculateInDegrees(final ProblemGraph problemGraph) {
    for (final List<ProblemNode> entry : problemGraph.getRepresentation().values()) {
      for (final ProblemNode n : entry) {
        inDegreeMap.merge(n, 1, Integer::sum);
      }
    }
  }

  @Override
  public boolean getsActivated(final ProblemNode node) {
    final int inDegree = inDegreeMap.get(node);
    final int max = 100 / inDegree;
    return random.nextInt(max + 1) == 1;
  }
}
