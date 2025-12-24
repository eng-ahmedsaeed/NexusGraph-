package org.example.Level_1;

import java.io.*;
import java.util.Map;
import java.util.Stack;

public class FormatingFile {

    
    public static void formatToFile(Map<Integer, String> fileData, String outputPath) {
        String formatted = formatToString(fileData);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(formatted);
        } catch (IOException e) {
            throw new RuntimeException("Error writing formatted XML: " + e.getMessage(), e);
        }
    }

    
    public static String formatToString(Map<Integer, String> fileData) {
        StringBuilder sb = new StringBuilder();
        Stack<String> tagStack = new Stack<>();
        int numTabs = 0;

        for (int i = 1; i <= fileData.size(); i++) {
            String line = fileData.get(i);
            if (line == null) continue;
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("</")) {
                numTabs--;
                appendIndented(sb, numTabs, line);
                if (!tagStack.isEmpty()) tagStack.pop();
            } else if (line.startsWith("<")) {
                if (line.contains("</")) {
                    appendIndented(sb, numTabs, line);
                } else {
                    appendIndented(sb, numTabs, line);
                    int endIdx = line.indexOf('>');
                    if (endIdx > 1) {
                        tagStack.push(line.substring(1, endIdx));
                    }
                    numTabs++;
                }
            } else {
                appendIndented(sb, numTabs, line);
            }
        }
        return sb.toString();
    }

    private static void appendIndented(StringBuilder sb, int tabs, String data) {
        for (int i = 0; i < tabs; i++) {
            sb.append("    ");
        }
        sb.append(data).append("\n");
    }
}
