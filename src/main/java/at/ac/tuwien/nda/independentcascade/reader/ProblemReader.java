package at.ac.tuwien.nda.independentcascade.reader;

import at.ac.tuwien.nda.independentcascade.valueobjects.Graph;

import java.io.*;

public class ProblemReader {

  private final File file;

  public ProblemReader(final File file) {
    this.file = file;
  }

  public Graph loadProblemInstance() throws IOException {

    final Graph graph = new Graph();
    try (BufferedReader reader =
                 new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {

      String currentLine = reader.readLine();
      while (currentLine != null) {
        if (!currentLine.startsWith("#")) {
          final int fromNode = Integer.parseInt(currentLine.split("\t")[0]);
          final int toNode = Integer.parseInt(currentLine.split("\t")[1]);
          graph.addNode(fromNode, toNode);
        }
        currentLine = reader.readLine();
      }
    }
    return graph;
  }
}
