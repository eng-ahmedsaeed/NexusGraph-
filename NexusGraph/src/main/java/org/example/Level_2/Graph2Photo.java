package org.example.Level_2;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph2Photo {

    private ArrayList<Pair<String, String>> edges;

    public Graph2Photo(ArrayList<Pair<String, String>> edges) {
        this.edges = edges;
    }

    
    private String quoteName(String name) {
        if (name == null) return "\"unknown\"";
        return "\"" + name.replace("\"", "\\\"") + "\"";
    }

    
    public void Graph2Photoprint(String extension) {
        String outputDir = "Graphs";
        String dotPath = outputDir + "/graph.dot";
        String imgPath = outputDir + "/graph." + extension;
        File graphDir = new File(outputDir);
        if (!graphDir.exists()) {
            graphDir.mkdirs();
        }
        Map<String, Integer> followerCount = new HashMap<>();
        for (Pair<String, String> e : edges) {
            String target = e.getValue();
            followerCount.put(target, followerCount.getOrDefault(target, 0) + 1);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        sb.append("  rankdir=TB;\n");  // Top to Bottom for better readability
        sb.append("  ratio=fill;\n");
        sb.append("  node [shape=circle, style=filled, fillcolor=lightblue, fontname=\"Arial\"];\n");
        sb.append("  edge [color=\"#333333\"];\n");
        for (Map.Entry<String, Integer> entry : followerCount.entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();
            double width = Math.min(0.5 + (count * 0.2), 2.0);
            sb.append("  ").append(quoteName(name))
              .append(" [width=").append(width)
              .append(", height=").append(width).append("];\n");
        }
        for (Pair<String, String> e : edges) {
            sb.append("  ").append(quoteName(e.getKey()))
              .append(" -> ").append(quoteName(e.getValue())).append(";\n");
        }
        sb.append("}\n");

        String dotContent = sb.toString();
        try (FileWriter writer = new FileWriter(dotPath)) {
            writer.write(dotContent);
        } catch (IOException e) {
            return;
        }
        try {
            MutableGraph graph = new Parser().read(dotContent);
            
            Format format;
            switch (extension.toLowerCase()) {
                case "svg":
                    format = Format.SVG;
                    break;
                case "png":
                default:
                    format = Format.PNG;
                    break;
            }
            
            Graphviz.fromGraph(graph)
                    .width(800)
                    .render(format)
                    .toFile(new File(imgPath));
            
        } catch (Exception e) {
        }
    }
    
    
    public void generateToPath(String outputPath, String extension) {
        Map<String, Integer> followerCount = new HashMap<>();
        for (Pair<String, String> e : edges) {
            String target = e.getValue();
            followerCount.put(target, followerCount.getOrDefault(target, 0) + 1);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        sb.append("  rankdir=TB;\n");
        sb.append("  ratio=fill;\n");
        sb.append("  node [shape=circle, style=filled, fillcolor=lightblue, fontname=\"Arial\"];\n");
        sb.append("  edge [color=\"#333333\"];\n");
        
        for (Map.Entry<String, Integer> entry : followerCount.entrySet()) {
            String name = entry.getKey();
            int count = entry.getValue();
            double width = Math.min(0.5 + (count * 0.2), 2.0);
            sb.append("  ").append(quoteName(name))
              .append(" [width=").append(width)
              .append(", height=").append(width).append("];\n");
        }
        
        for (Pair<String, String> e : edges) {
            sb.append("  ").append(quoteName(e.getKey()))
              .append(" -> ").append(quoteName(e.getValue())).append(";\n");
        }
        sb.append("}\n");

        String dotContent = sb.toString();
        try {
            MutableGraph graph = new Parser().read(dotContent);
            
            Format format;
            switch (extension.toLowerCase()) {
                case "svg":
                    format = Format.SVG;
                    break;
                case "jpg":
                case "jpeg":
                    format = Format.PNG; // Graphviz-Java uses PNG, we can rename
                    break;
                case "png":
                default:
                    format = Format.PNG;
                    break;
            }
            
            Graphviz.fromGraph(graph)
                    .width(800)
                    .render(format)
                    .toFile(new File(outputPath));
            
        } catch (Exception e) {
            System.err.println("Error generating graph: " + e.getMessage());
        }
    }
}
