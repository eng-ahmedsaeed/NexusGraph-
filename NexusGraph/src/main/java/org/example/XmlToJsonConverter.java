package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmlToJsonConverter {

    private static final int INDENT_SPACES = 2;

    public void convertFile(String inputPath, String outputPath) throws IOException {
        String xml = readFile(inputPath);
        String json = convert(xml);
        writeFile(outputPath, json);
    }

    public String convert(String xml) {
        if (xml == null || xml.isEmpty()) {
            throw new IllegalArgumentException("XML input cannot be null or empty");
        }

        Node root = parse(xml);
        Map<String, Object> output = new LinkedHashMap<>();
        output.put(root.name, toJsonValue(root));

        StringBuilder sb = new StringBuilder();
        writeJson(output, sb, 0);
        sb.append('\n');
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        Path classFolder = Path.of("src", "main", "java", "org", "example");
        String inputPath = classFolder.resolve("input_file.xml").toString();
        String outputPath = classFolder.resolve("output_file.json").toString();
        new XmlToJsonConverter().convertFile(inputPath, outputPath);
        System.out.println("JSON saved to " + outputPath);
    }

    private static String readFile(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        }
        return sb.toString();
    }

    private static void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    private static final class Node {
        private final String name;
        private final List<Node> children = new ArrayList<>();
        private final StringBuilder text = new StringBuilder();

        private Node(String name) {
            this.name = name;
        }
    }

    private static Node parse(String xml) {
        int i = 0;
        Deque<Node> stack = new ArrayDeque<>();
        Node root = null;
        StringBuilder textBuffer = new StringBuilder();

        while (i < xml.length()) {
            char c = xml.charAt(i);
            if (c != '<') {
                textBuffer.append(c);
                i++;
                continue;
            }

            if (!stack.isEmpty() && textBuffer.length() > 0) {
                stack.peek().text.append(textBuffer);
                textBuffer.setLength(0);
            }

            if (xml.startsWith("<!--", i)) {
                int end = xml.indexOf("-->", i + 4);
                if (end < 0) {
                    throw new IllegalArgumentException("Unclosed XML comment");
                }
                i = end + 3;
                continue;
            }

            int close = xml.indexOf('>', i + 1);
            if (close < 0) {
                throw new IllegalArgumentException("Malformed XML: missing '>'");
            }

            String tagContent = xml.substring(i + 1, close).trim();
            if (tagContent.isEmpty()) {
                throw new IllegalArgumentException("Malformed XML: empty tag");
            }

            if (tagContent.charAt(0) == '?') {
                i = close + 1;
                continue;
            }

            if (tagContent.charAt(0) == '!') {
                i = close + 1;
                continue;
            }

            boolean selfClosing = tagContent.endsWith("/");
            if (selfClosing) {
                tagContent = tagContent.substring(0, tagContent.length() - 1).trim();
            }

            if (tagContent.charAt(0) == '/') {
                String closingName = tagContent.substring(1).trim();
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Malformed XML: unexpected closing tag </" + closingName + ">");
                }

                Node node = stack.pop();
                if (!node.name.equals(closingName)) {
                    throw new IllegalArgumentException(
                            "Malformed XML: closing tag </" + closingName + "> does not match <" + node.name + ">"
                    );
                }

                i = close + 1;
                continue;
            }

            String openingName = tagContent;
            Node node = new Node(openingName);

            if (stack.isEmpty()) {
                if (root != null) {
                    throw new IllegalArgumentException("Malformed XML: multiple root elements");
                }
                root = node;
            } else {
                stack.peek().children.add(node);
            }

            if (!selfClosing) {
                stack.push(node);
            }

            i = close + 1;
        }

        if (!stack.isEmpty()) {
            throw new IllegalArgumentException("Malformed XML: unclosed tag <" + stack.peek().name + ">");
        }

        if (root == null) {
            throw new IllegalArgumentException("Malformed XML: no root element found");
        }

        return root;
    }

    private static Object toJsonValue(Node node) {
        if (node.children.isEmpty()) {
            return normalizeText(node.text.toString());
        }

        if (allChildrenHaveSameName(node.children)
                && (node.children.size() > 1 || isPluralContainerForChild(node.name, node.children.get(0).name))) {
            List<Object> arr = new ArrayList<>();
            for (Node child : node.children) {
                arr.add(toJsonValue(child));
            }
            return arr;
        }

        Map<String, Object> obj = new LinkedHashMap<>();
        for (Node child : node.children) {
            Object value = toJsonValue(child);
            Object existing = obj.get(child.name);

            if (existing == null) {
                obj.put(child.name, value);
                continue;
            }

            if (existing instanceof List) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) existing;
                list.add(value);
            } else {
                List<Object> list = new ArrayList<>();
                list.add(existing);
                list.add(value);
                obj.put(child.name, list);
            }
        }

        return obj;
    }

    private static boolean allChildrenHaveSameName(List<Node> children) {
        String first = children.get(0).name;
        for (int i = 1; i < children.size(); i++) {
            if (!children.get(i).name.equals(first)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isPluralContainerForChild(String containerName, String childName) {
        if (containerName == null || childName == null) {
            return false;
        }
        return containerName.equals(childName + "s");
    }

    private static String normalizeText(String text) {
        String trimmed = text.trim();
        if (trimmed.isEmpty()) {
            return "";
        }
        return trimmed.replaceAll("\\s+", " ");
    }

    private static void writeJson(Object value, StringBuilder sb, int indent) {
        if (value instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) value;

            sb.append("{");
            if (map.isEmpty()) {
                sb.append("}");
                return;
            }

            sb.append('\n');
            int index = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                indent(sb, indent + INDENT_SPACES);
                sb.append('"').append(escapeJsonString(entry.getKey())).append('"').append(": ");
                writeJson(entry.getValue(), sb, indent + INDENT_SPACES);

                if (index < map.size() - 1) {
                    sb.append(',');
                }
                sb.append('\n');
                index++;
            }

            indent(sb, indent);
            sb.append("}");
            return;
        }

        if (value instanceof List) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) value;

            sb.append("[");
            if (list.isEmpty()) {
                sb.append("]");
                return;
            }

            sb.append('\n');
            for (int i = 0; i < list.size(); i++) {
                indent(sb, indent + INDENT_SPACES);
                writeJson(list.get(i), sb, indent + INDENT_SPACES);
                if (i < list.size() - 1) {
                    sb.append(',');
                }
                sb.append('\n');
            }

            indent(sb, indent);
            sb.append("]");
            return;
        }

        sb.append('"').append(escapeJsonString(String.valueOf(value))).append('"');
    }

    private static void indent(StringBuilder sb, int spaces) {
        for (int i = 0; i < spaces; i++) {
            sb.append(' ');
        }
    }

    private static String escapeJsonString(String s) {
        StringBuilder out = new StringBuilder(s.length() + 16);
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"' -> out.append("\\\"");
                case '\\' -> out.append("\\\\");
                case '\b' -> out.append("\\b");
                case '\f' -> out.append("\\f");
                case '\n' -> out.append("\\n");
                case '\r' -> out.append("\\r");
                case '\t' -> out.append("\\t");
                default -> {
                    if (c < 0x20) {
                        out.append(String.format("\\u%04x", (int) c));
                    } else {
                        out.append(c);
                    }
                }
            }
        }
        return out.toString();
    }
}
