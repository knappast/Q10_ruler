package se.kaskware.bakaxel;

import se.kaskware.bakaxel.nodes.SystemNode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Proudly produced by APLE02 on 2017-02-21 17:32. */
public class SystemFileParser {
  public static void main(String args[]) {
    SystemFileParser parser = new SystemFileParser();

    List<SystemNode> sysList = parser.parse("System_lista.csv");
    System.out.println("sysList = " + sysList.size());
  }

  private List<SystemNode> parse(String fileName) {
    ClassLoader classLoader = getClass().getClassLoader();
    String path = new File(classLoader.getResource(fileName).getFile()).getPath();

    List<SystemNode> sysList = null; // = new ArrayList();
    try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.ISO_8859_1)) {
//      stream.forEach((String line) -> sysList.add(new SystemNode(line)));
      sysList = stream.map(line -> new SystemNode(line)).collect(Collectors.toList());
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    return sysList;
  }
}
