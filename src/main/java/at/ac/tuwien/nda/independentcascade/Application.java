package at.ac.tuwien.nda.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.activationfunctions.UniformActivation;
import at.ac.tuwien.nda.independentcascade.independentcascade.IndependentCascadeModel;
import at.ac.tuwien.nda.independentcascade.reader.ProblemReader;
import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;
import at.ac.tuwien.nda.independentcascade.valueobjects.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;


public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    final Activationable activation = new UniformActivation(1);
    final String fileName = Application.class.getClassLoader().getResource("Cit-HepPh.txt").getPath();
    final File file = new File(fileName);
    final ProblemReader reader = new ProblemReader(file);
    try {
      final Graph graph = reader.loadProblemInstance();
      final IndependentCascadeModel model = new IndependentCascadeModel(graph, activation, 2);
      final Set<Node> activeNodes = model.calculateActiveNodes();
      activeNodes.forEach(System.out::println);
    } catch (IOException e) {
      logger.error("error reading problem instance", e);
    }
  }
}
