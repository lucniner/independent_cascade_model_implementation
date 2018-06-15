package at.ac.tuwien.nda.independentcascade.activationfunctions;

import java.util.Random;

public class UniformActivation implements Activationable {

  private final Random random = new Random();
  private final int probability;

  public UniformActivation(final int probability) {
    this.probability = probability;
  }

  @Override
  public double getProbability() {
    return probability / 100.0;
  }

  @Override
  public boolean getsActivated() {
    final int max = 100 / probability;
    return random.nextInt(max + 1) == 1;
  }
}
