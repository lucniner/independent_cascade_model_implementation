package at.ac.tuwien.nda.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.activationfunctions.UniformActivation;
import at.ac.tuwien.nda.independentcascade.independentcascade.IndependentCascadeModel;
import at.ac.tuwien.nda.independentcascade.independentcascade.IndependentCascadeModelAlternative;
import at.ac.tuwien.nda.independentcascade.reader.ProblemReader;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Set;


public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Instant begin = Instant.now();
    final Activationable activation = new UniformActivation(1);
    final String fileName = Application.class.getClassLoader().getResource("Cit-HepPh.txt").getPath();
    final File file = new File(fileName);
    final ProblemReader reader = new ProblemReader(file);
    try {
      final Graph graph = reader.loadProblemInstance();
      //final IndependentCascadeModel model = new IndependentCascadeModel(graph, activation, 3);
      //final Set<Node> activeNodes = model.calculateActiveNodes();
      IndependentCascadeModelAlternative model = new IndependentCascadeModelAlternative(graph, activation, 6, 5);
      final Pair<Integer, Set<Node>> seedSet = model.run();
      System.out.println("Nodes activated: " + seedSet.getKey());
      System.out.println("Seed set:");
      seedSet.getValue().stream().map(value -> value.getId()).forEach(System.out::println);
      System.out.println("Running time [seconds]: " + (Instant.now().getEpochSecond() - begin.getEpochSecond()));
    } catch (IOException e) {
      logger.error("error reading problem instance", e);
    }
  }
}
