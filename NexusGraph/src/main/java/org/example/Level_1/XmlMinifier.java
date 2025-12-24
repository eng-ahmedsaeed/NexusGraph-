package org.example.Level_1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class XmlMinifier {

    
    public static void minifyToFile(Map<Integer, String> codeLines, String outputPath) {
        String minified = minifyToString(codeLines);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write(minified);
        } catch (IOException e) {
            throw new RuntimeException("Error writing minified XML: " + e.getMessage(), e);
        }
    }

    
    public static String minifyToString(Map<Integer, String> codeLines) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= codeLines.size(); i++) {
            String line = codeLines.get(i);
            if (line != null) {
                sb.append(line.trim());
            }
        }
        return sb.toString();
    }
}
