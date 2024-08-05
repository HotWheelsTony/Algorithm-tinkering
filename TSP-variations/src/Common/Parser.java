package Common;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class to deal with parsing input files
 */
public class Parser {

    public static Map<Integer, Node> parseFile(String path) {
        Map<Integer, Node> nodes = new HashMap<>();

        try (Stream<String> fileStream = Files.lines(Path.of(path))) {
            nodes = fileStream
                    .skip(6)
                    .filter(line -> !line.toLowerCase().contains("eof"))
                    .map(line -> line.trim().split("\\s+"))
                    .collect(Collectors.toMap(
                            data -> Integer.parseInt(data[0]),
                            data -> new Node(
                                    Integer.parseInt(data[0]),
                                    Integer.parseInt(data[1]),
                                    Integer.parseInt(data[2])
                            )
                    ));

        } catch (IOException e) {
            System.out.println("Error parsing file: " + e.getMessage());
        }

        return nodes;
    }
}
