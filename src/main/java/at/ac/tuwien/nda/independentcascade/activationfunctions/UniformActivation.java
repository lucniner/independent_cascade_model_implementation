package at.ac.tuwien.nda.independentcascade.activationfunctions;

import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;

import java.util.Random;

public class UniformActivation implements Activationable {

  private final Random random = new Random();
  private final int probability;

  public UniformActivation(final int probability) {
    this.probability = probability;
  }


  @Override
  public boolean getsActivated(final ProblemNode node) {
    final int max = 100 / probability;
    return random.nextInt(max + 1) == 1;
  }
}
