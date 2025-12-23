import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class Xml_Rearder {

    private Map<Integer, String> linesMap;
    String filePath;

    public Xml_Rearder(String filePath) {
        linesMap = new LinkedHashMap<>();
        this.filePath = filePath;
    }

    public void readFile(String filePath) {
        // Reuse parseString to avoid duplicate line parsing logic
        String content = readFileToString(filePath);
        linesMap = parseString(content);
    }

    /**
     * Reads file contents to a single String.
     */
    public static String readFileToString(String filePath) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (sb.length() > 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return sb.toString();
    }

    public Map<Integer, String> getLinesMap() {
        return linesMap;
    }

    /**
     * Converts a String to a Map<Integer, String> where each line is numbered.
     */
    public static Map<Integer, String> parseString(String content) {
        Map<Integer, String> map = new LinkedHashMap<>();
        if (content == null || content.isEmpty()) {
            return map;
        }
        String[] lines = content.split("\n");
        for (int i = 0; i < lines.length; i++) {
            map.put(i + 1, lines[i].replace("\r", ""));
        }
        return map;
    }

    /**
     * Converts a Map<Integer, String> back to a single String.
     */
    public static String mapToString(Map<Integer, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        
        // Find the maximum key to handle sparse maps
        int maxKey = 0;
        for (Integer key : map.keySet()) {
            if (key > maxKey) maxKey = key;
        }
        
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (int i = 1; i <= maxKey; i++) {
            String line = map.get(i);
            if (line != null) {
                if (!first) {
                    sb.append("\n");
                }
                sb.append(line);
                first = false;
            }
        }
        return sb.toString();
    }
}
