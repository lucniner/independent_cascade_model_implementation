package at.ac.tuwien.nda.independentcascade;

import at.ac.tuwien.nda.independentcascade.activationfunctions.Activationable;
import at.ac.tuwien.nda.independentcascade.activationfunctions.UniformActivation;
import at.ac.tuwien.nda.independentcascade.independentcascade.IndependentCascadeModel;
import at.ac.tuwien.nda.independentcascade.reader.ProblemReader;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;
import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemNode;
import at.ac.tuwien.nda.independentcascade.visualization.Visualizer;
import org.apache.commons.cli.*;
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
    final CommandLine cmd = parseArguments(args);

    Integer probability = 1;
    Integer budget = 10;
    Integer scenarios = 10;

    if (cmd.hasOption("probability")) {
      probability = Integer.parseInt(cmd.getOptionValue("probability"));
    }
    logger.info("simulating with probability of {}", probability);

    if (cmd.hasOption("budget")) {
      budget = Integer.parseInt(cmd.getOptionValue("budget"));
    }

    logger.info("simulating with budget of {}", budget);


    if (cmd.hasOption("scenarios")) {
      scenarios = Integer.parseInt(cmd.getOptionValue("scenarios"));
    }

    logger.info("simulating with {} scenarios", scenarios);


    final Activationable activation = new UniformActivation(probability);
    final InputStream inputStream =
            Application.class.getClassLoader().getResourceAsStream("Cit-HepPh.txt");
    final ProblemReader reader = new ProblemReader(inputStream);
    try {
      final ProblemGraph problemGraph = reader.loadProblemInstance();
      logger.info("read problem graph");
      final IndependentCascadeModel model =
              new IndependentCascadeModel(problemGraph, activation, budget, scenarios);

      final Instant begin = Instant.now();
      logger.info("starting influence maximization simulation");
      final Pair<Integer, Set<ProblemNode>> seedSet = model.run();

      logger.info("Nodes activated: " + seedSet.getKey());

      final Set<Integer> seeds =
              seedSet.getValue().stream().map(ProblemNode::getId).collect(Collectors.toSet());

      logger.info("Seed set:  {}", seeds);
      logger.info(
              "Running time [seconds]: " + (Instant.now().getEpochSecond() - begin.getEpochSecond()));

      if (cmd.hasOption("visualize")) {
        final Visualizer visualizer = new Visualizer(problemGraph, seeds, model.getAlreadyActivated());
        visualizer.createGraph();
      }
    } catch (IOException e) {
      logger.error("error reading problem instance", e);
    }
  }

  private static CommandLine parseArguments(String[] args) {
    CommandLine cmd = null;

    Options options = new Options();
    Option help = new Option("h", "help", false, "print this message");
    Option visualize = new Option("v", "visualize", false, "visualization of the output");
    Option probability =
            new Option("p", "probability", true, "the uniform probability, value between 1 and 100");
    Option budget =
            new Option("b", "budget", true, "how many seed nodes should be chosen value > 0");
    Option scenarios =
            new Option("s", "scenarios", true, "how many scenarios should be created value > 0");

    options.addOption(help);
    options.addOption(visualize);
    options.addOption(probability);
    options.addOption(budget);
    options.addOption(scenarios);

    try {
      cmd = new DefaultParser().parse(options, args, false);
    } catch (ParseException e) {
      logger.error("parser exception", e);
      new HelpFormatter().printHelp("java -jar <name>", options, true);
      System.exit(0);
    }

    if (cmd.hasOption("help")) {
      new HelpFormatter().printHelp("java -jar <name>", options, true);
      System.exit(0);
    }

    return cmd;
  }
}
