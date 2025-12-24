package org.example.Level_1;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class XMLDecompressor {

    private Map<String, String> tagMappings;
    public XMLDecompressor() {
        this.tagMappings = new HashMap<>();
    }
    public String formatXML(String xml) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        String indent = "    "; // 4 spaces
        
        int i = 0;
        while (i < xml.length()) {
            while (i < xml.length() && Character.isWhitespace(xml.charAt(i))) {
                i++;
            }
            if (i >= xml.length()) break;
            
            if (xml.charAt(i) == '<') {
                int tagEnd = xml.indexOf('>', i);
                if (tagEnd < 0) break;
                
                String tag = xml.substring(i, tagEnd + 1);
                
                if (tag.startsWith("</")) {
                    indentLevel--;
                    appendIndent(formatted, indentLevel, indent);
                    formatted.append(tag).append("\n");
                } else if (tag.endsWith("/>")) {
                    appendIndent(formatted, indentLevel, indent);
                    formatted.append(tag).append("\n");
                } else if (tag.startsWith("<?") || tag.startsWith("<!")) {
                    appendIndent(formatted, indentLevel, indent);
                    formatted.append(tag).append("\n");
                } else {
                    int nextTagStart = xml.indexOf('<', tagEnd + 1);
                    String textContent = "";
                    if (nextTagStart > tagEnd + 1) {
                        textContent = xml.substring(tagEnd + 1, nextTagStart).trim();
                    }
                    if (nextTagStart >= 0 && nextTagStart < xml.length()) {
                        String tagName = tag.substring(1, tag.length() - 1).split("\\s")[0];
                        String expectedClosing = "</" + tagName + ">";
                        
                        if (xml.substring(nextTagStart).startsWith(expectedClosing)) {
                            appendIndent(formatted, indentLevel, indent);
                            formatted.append(tag).append(textContent).append(expectedClosing).append("\n");
                            i = nextTagStart + expectedClosing.length();
                            continue;
                        }
                    }
                    appendIndent(formatted, indentLevel, indent);
                    formatted.append(tag).append("\n");
                    indentLevel++;
                }
                
                i = tagEnd + 1;
            } else {
                int nextTag = xml.indexOf('<', i);
                if (nextTag < 0) nextTag = xml.length();
                String text = xml.substring(i, nextTag).trim();
                if (!text.isEmpty()) {
                    appendIndent(formatted, indentLevel, indent);
                    formatted.append(text).append("\n");
                }
                i = nextTag;
            }
        }
        
        return formatted.toString();
    }
    public String formatJSON(String json) {
        StringBuilder formatted = new StringBuilder();
        int indentLevel = 0;
        String indent = "  ";
        boolean inString = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
                formatted.append(c);
                continue;
            }
            if (inString) {
                formatted.append(c);
                continue;
            }
            
            switch (c) {
                case '{':
                case '[':
                    formatted.append(c);
                    formatted.append('\n');
                    indentLevel++;
                    appendIndent(formatted, indentLevel, indent);
                    break;
                    
                case '}':
                case ']':
                    formatted.append('\n');
                    indentLevel--;
                    appendIndent(formatted, indentLevel, indent);
                    formatted.append(c);
                    break;
                    
                case ',':
                    formatted.append(c);
                    formatted.append('\n');
                    appendIndent(formatted, indentLevel, indent);
                    break;
                    
                case ':':
                    formatted.append(c);
                    formatted.append(' ');
                    break;
                    
                case ' ':
                case '\t':
                case '\n':
                case '\r':
                    break;
                    
                default:
                    formatted.append(c);
            }
        }
        
        return formatted.toString();
    }
    
    private void appendIndent(StringBuilder sb, int level, String indent) {
        for (int i = 0; i < level; i++) {
            sb.append(indent);
        }
    }
    public void loadKeyFile(String keyFilePath) throws IOException {
        String content = Xml_Rearder.readFileToString(keyFilePath);
        if (content.isEmpty()) {
            throw new IOException("Key file not found or empty: " + keyFilePath);
        }
        
        String[] lines = content.split("\n");
        for (String line : lines) {
            line = line.replace("\r", "");
            if (line.length() >= 3) {
                String shortTag = line.substring(0, 2); // Short tag is the first two characters
                String fullTag = line.substring(2); // The rest of the line is the full tag
                tagMappings.put(shortTag, fullTag);
            }
        }
    }
    public String decompress(String compressedXML) {
        if (compressedXML == null || compressedXML.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        String decompressed = replaceTags(compressedXML);
        decompressed = formatXML(decompressed);

        return decompressed;
    }
    public String decompressJSON(String compressedJSON) {
        if (compressedJSON == null || compressedJSON.isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        String decompressed = replaceTags(compressedJSON);
        decompressed = formatJSON(decompressed);

        return decompressed;
    }
    private String replaceTags(String input) {
        for (Map.Entry<String, String> entry : tagMappings.entrySet()) {
            String shortTag = entry.getKey();
            String fullTag = entry.getValue();
            input = input.replace(shortTag, fullTag);
        }
        return input;
    }
    public String readFile(String filePath) throws IOException {
        String content = Xml_Rearder.readFileToString(filePath);
        if (content.isEmpty()) {
            throw new IOException("Compressed file not found or empty: " + filePath);
        }
        return content.replace("\n", "").replace("\r", "");
    }
    public void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }
}
