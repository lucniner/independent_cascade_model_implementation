package at.ac.tuwien.nda.independentcascade.reader;

import at.ac.tuwien.nda.independentcascade.valueobjects.ProblemGraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProblemReader {

  private final InputStream inputStream;

  public ProblemReader(final InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public ProblemGraph loadProblemInstance() throws IOException {

    final ProblemGraph problemGraph = new ProblemGraph();
    try (BufferedReader reader =
                 new BufferedReader(new InputStreamReader(inputStream))) {

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
