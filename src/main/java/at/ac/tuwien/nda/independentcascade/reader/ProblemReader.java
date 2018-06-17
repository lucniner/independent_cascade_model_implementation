package at.ac.tuwien.nda.independentcascade.reader;

import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;

import java.io.*;

public class ProblemReader {

  private final File file;

  public ProblemReader(final File file) {
    this.file = file;
  }

  public ProblemGraph loadProblemInstance() throws IOException {

    final ProblemGraph problemGraph = new ProblemGraph();
    try (BufferedReader reader =
                 new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

      String currentLine = reader.readLine();
      while (currentLine != null) {
        if (!currentLine.startsWith("#")) {
          final int fromNode = Integer.parseInt(currentLine.split("\t")[0]);
          final int toNode = Integer.parseInt(currentLine.split("\t")[1]);
          problemGraph.addNode(fromNode, toNode);
        }
        currentLine = reader.readLine();
      }
    }
    return problemGraph;
  }
}
