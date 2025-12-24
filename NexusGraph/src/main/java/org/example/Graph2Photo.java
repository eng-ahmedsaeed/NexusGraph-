package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Graph2Photo {

    private ArrayList<Pair<String, String>> edges;

    Graph2Photo(ArrayList<Pair<String, String>> edges) {
        this.edges = edges;
    }

    void Graph2Photoprint(String extension) {

        String outputDir = "graphs";   // Relative path
        String dotPath = outputDir + "/graph.dot";
        String imgPath = outputDir + "/graph." + extension;

        // 1️⃣ Build DOT text
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        sb.append("rankdir=LR; \n");
        sb.append("ratio=fill; \n");
        for (Pair<String, String> e : edges) {
            sb.append("  ").append(e.toString()).append(";\n");
        }

        sb.append("}\n");


        try (FileWriter writer = new FileWriter(dotPath)) {
            writer.write(sb.toString());
        } catch (IOException e) {
            System.out.println("Error writing DOT file: " + e.getMessage());
            return;
        }


        try {
            Process process = new ProcessBuilder(
                    "dot",
                    "-T" + extension,
                    dotPath,
                    "-o",
                    imgPath
            ).inheritIO().start();

            process.waitFor();

        } catch (Exception e) {
            System.out.println("Error running Graphviz: " + e.getMessage());
        }
    }
}
