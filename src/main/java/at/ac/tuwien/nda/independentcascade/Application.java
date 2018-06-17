package at.ac.tuwien.nda.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.activationfunctions.UniformActivation;
import at.ac.tuwien.nda.independentcascade.independentcascade.IndependentCascadeModel;
import at.ac.tuwien.nda.independentcascade.reader.ProblemReader;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;


public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    final Activationable activation = new UniformActivation(1);
    final InputStream inputStream = Application.class.getClassLoader().getResourceAsStream("Cit-HepPh.txt");
    final ProblemReader reader = new ProblemReader(inputStream);
    try {
      final ProblemGraph problemGraph = reader.loadProblemInstance();
      final IndependentCascadeModel model = new IndependentCascadeModel(problemGraph, activation, 10, 1);

      final Instant begin = Instant.now();
      final Pair<Integer, Set<ProblemNode>> seedSet = model.run();

      logger.info("Nodes activated: " + seedSet.getKey());

      final Set<Integer> seeds = seedSet.getValue().stream().map(ProblemNode::getId).collect(Collectors.toSet());

      logger.info("Seed set:  {}", seeds);
      logger.info("Running time [seconds]: " + (Instant.now().getEpochSecond() - begin.getEpochSecond()));
    } catch (IOException e) {
      logger.error("error reading problem instance", e);
    }
  }
}
