package org.example.Level_1;

import org.example.Level_2.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XmlEditorCLI {

    public void run(String[] args) {

        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        String inputFile = null;
        String outputFile = null;
        boolean fix = false;
        String type = "xml";  // Default type is XML, can be "json"
        String word = null;
        String topic = null;
        String ids = null;
        String userId = null;
        for (int i = 1; i < args.length; i++) {
            String arg = args[i];

            switch (arg) {
                case "-i":
                    if (i + 1 < args.length)
                        inputFile = args[++i];
                    else
                        error("Missing input file after -i");
                    break;

                case "-o":
                    if (i + 1 < args.length)
                        outputFile = args[++i];
                    else
                        error("Missing output file after -o");
                    break;

                case "-f":
                    fix = true;
                    break;

                case "-type":
                    if (i + 1 < args.length) {
                        type = args[++i].toLowerCase();
                        if (!type.equals("xml") && !type.equals("json")) {
                            error("Type must be 'xml' or 'json'");
                        }
                    } else {
                        error("Missing type after -type");
                    }
                    break;
                case "-w":
                    if (i + 1 < args.length)
                        word = args[++i];
                    else
                        error("Missing word after -w");
                    break;
                    
                case "-t":
                    if (i + 1 < args.length)
                        topic = args[++i];
                    else
                        error("Missing topic after -t");
                    break;
                    
                case "-ids":
                    if (i + 1 < args.length)
                        ids = args[++i];
                    else
                        error("Missing ids after -ids");
                    break;
                    
                case "-id":
                    if (i + 1 < args.length)
                        userId = args[++i];
                    else
                        error("Missing id after -id");
                    break;

                default:
                    error("Unknown option: " + arg);
            }
        }

        if (inputFile == null) {
            error("Input file is required. Use -i <file>");
        }
        else
        {
            inputFile = resolveFilePath(inputFile);
        }
        boolean isJsonInput = inputFile != null && inputFile.toLowerCase().endsWith(".json");
        if (isJsonInput && type.equals("xml")) {
            type = "json";
        }
        switch (command) {

            case "verify":
                if (isJsonInput) {
                    error("Verify is only available for XML files.\nFor JSON files, only 'compress' and 'decompress' commands are available.");
                }
                verifyXml(inputFile, outputFile, fix);
                break;

            case "format":
                if (isJsonInput) {
                    error("Format is only available for XML files.\nFor JSON files, only 'compress' and 'decompress' commands are available.");
                }
                requireOutput(outputFile);
                formatXml(inputFile, outputFile);
                break;

            case "json":
                if (isJsonInput) {
                    error("Convert to JSON is only available for XML files.\nThe input file is already in JSON format.");
                }
                requireOutput(outputFile);
                convertToJson(inputFile, outputFile);
                break;

            case "mini":
                if (isJsonInput) {
                    error("Minify is only available for XML files.\nFor JSON files, only 'compress' and 'decompress' commands are available.");
                }
                requireOutput(outputFile);
                minifyXml(inputFile, outputFile);
                break;

            case "compress":
                requireOutput(outputFile);
                if (type.equals("json")) {
                    compressJsonFile(inputFile, outputFile);
                } else {
                    compressFile(inputFile, outputFile);
                }
                break;

            case "decompress":
                requireOutput(outputFile);
                if (type.equals("json")) {
                    decompressJsonFile(inputFile, outputFile);
                } else {
                    decompressFile(inputFile, outputFile);
                }
                break;
            
            case "draw":
                if (isJsonInput) {
                    error("Draw is only available for XML files.");
                }
                drawGraph(inputFile, outputFile);
                break;
                
            case "most_active":
                if (isJsonInput) {
                    error("Most active is only available for XML files.");
                }
                mostActive(inputFile);
                break;
                
            case "most_influencer":
                if (isJsonInput) {
                    error("Most influencer is only available for XML files.");
                }
                mostInfluencer(inputFile);
                break;
                
            case "mutual":
                if (isJsonInput) {
                    error("Mutual is only available for XML files.");
                }
                if (ids == null) {
                    error("mutual requires -ids <id1,id2,...>");
                }
                mutualUsers(inputFile, ids);
                break;
                
            case "suggest":
                if (isJsonInput) {
                    error("Suggest is only available for XML files.");
                }
                if (userId == null) {
                    error("suggest requires -id <userId>");
                }
                suggestUsers(inputFile, userId);
                break;
                
            case "search":
                if (isJsonInput) {
                    error("Search is only available for XML files.");
                }
                if (word != null && topic != null) {
                    error("Use either -w or -topic, not both");
                }
                if (word != null) {
                    searchByWord(inputFile, word);
                } else if (topic != null) {
                    searchByTopic(inputFile, topic);
                } else {
                    error("search requires -w <word> or -topic <topic>");
                }
                break;

            default:
                error("Unknown command: " + command);
        }
    }

    private void printUsage() {
        System.out.println("\nXML Editor CLI - Usage:");
        System.out.println("  xml_editor <command> [options]");
        System.out.println("\nLevel 1 Commands:");
        System.out.println("  verify       - Validate XML file");
        System.out.println("  format       - Format/prettify XML");
        System.out.println("  json         - Convert XML to JSON");
        System.out.println("  mini         - Minify XML");
        System.out.println("  compress     - Compress file (XML or JSON)");
        System.out.println("  decompress   - Decompress file (XML or JSON)");
        System.out.println("\nLevel 2 Commands (Network Analysis):");
        System.out.println("  draw           - Generate network graph image");
        System.out.println("  most_active    - Find most active user(s)");
        System.out.println("  most_influencer - Find most influential user");
        System.out.println("  mutual         - Find mutual followers");
        System.out.println("  suggest        - Suggest users to follow");
        System.out.println("  search         - Search posts by word or topic");
        System.out.println("\nOptions:");
        System.out.println("  -i <file>    Input file (required)");
        System.out.println("  -o <file>    Output file");
        System.out.println("  -f           Fix errors (verify only)");
        System.out.println("  -t <type>    Type: 'xml' or 'json' (default: xml)");
        System.out.println("  -ids <list>  Comma-separated user IDs (mutual)");
        System.out.println("  -id <id>     Single user ID (suggest)");
        System.out.println("  -w <word>    Search by word (search)");
        System.out.println("  -topic <t>   Search by topic (search)");
        System.out.println("\nExamples:");
        System.out.println("  xml_editor verify -i input.xml");
        System.out.println("  xml_editor verify -i input.xml -f -o fixed.xml");
        System.out.println("  xml_editor format -i input.xml -o output.xml");
        System.out.println("  xml_editor draw -i network.xml");
        System.out.println("  xml_editor most_active -i network.xml");
        System.out.println("  xml_editor mutual -i network.xml -ids 1,2,3");
        System.out.println("  xml_editor suggest -i network.xml -id 1");
        System.out.println("  xml_editor search -i network.xml -w hello");
        System.out.println("  xml_editor search -i network.xml -topic sports\n");
    }

    private void error(String msg) {
        System.err.println("Error: " + msg);
        System.exit(1);
    }

    private void requireOutput(String out) {
        if (out == null) error("Output file is required. Use -o <file>");
    }

    
    private String resolveFilePath(String filename) {
        java.io.File file = new java.io.File(filename);
        if (file.isAbsolute() && file.exists()) {
            System.out.println("File found: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        }
        file = new java.io.File(filename);
        if (file.exists()) {
            System.out.println("File found: " + file.getAbsolutePath());
            return file.getAbsolutePath();
        }
        
        String justFilename = new java.io.File(filename).getName();
        System.out.println("Searching entire computer for: " + justFilename + " (this may take a while...)");
        java.io.File[] roots = java.io.File.listRoots();
        for (java.io.File root : roots) {
            System.out.println("Searching drive: " + root.getAbsolutePath());
            java.io.File found = findFileRecursive(root, justFilename);
            if (found != null) {
                System.out.println("File found: " + found.getAbsolutePath());
                return found.getAbsolutePath();
            }
        }
        System.out.println("Warning: File not found anywhere on the computer");
        return Path.of(filename).toAbsolutePath().toString();
    }
    
    
    private java.io.File findFileRecursive(java.io.File dir, String filename) {
        if (dir == null || !dir.isDirectory()) return null;
        
        try {
            java.io.File[] files = dir.listFiles();
            if (files == null) return null;
            for (java.io.File file : files) {
                if (file.isFile() && file.getName().equalsIgnoreCase(filename)) {
                    return file;
                }
            }
            for (java.io.File file : files) {
                String name = file.getName();
                if (file.isDirectory() && !name.startsWith(".") && !name.startsWith("$") 
                    && !name.equals("Windows") && !name.equals("Program Files") 
                    && !name.equals("Program Files (x86)") && !name.equals("ProgramData")) {
                    java.io.File found = findFileRecursive(file, filename);
                    if (found != null) return found;
                }
            }
        } catch (Exception e) {
        }
        
        return null;
    }

    private void verifyXml(String in, String out, boolean fix) {
        Xml_Rearder reader = new Xml_Rearder(in);
        reader.readFile(in);
        Map<Integer, String> xmlMap = reader.getLinesMap();
        if (xmlMap.isEmpty()) {
            error("Failed to read input file or file is empty: " + in);
            return;
        }

        XMLValidator validator = new XMLValidator(new HashMap<>(xmlMap));
        validator.validate();

        if (validator.errors.isEmpty()) {
            System.out.println("XML is valid! No errors found.");
        } else {
            System.out.println("XML Validation Errors:");
            java.util.TreeMap<Integer, String> sortedErrors = new java.util.TreeMap<>(validator.errors);
            for (Map.Entry<Integer, String> entry : sortedErrors.entrySet()) {
                System.out.println("  Line " + entry.getKey() + ": " + entry.getValue());
            }

            if (fix) {
                HashMap<Integer, String> fixedXml = validator.applyFixes();
                String fixedContent = Xml_Rearder.mapToString(fixedXml);
                if (out != null) {
                    writeToFile(out, fixedContent);
                    System.out.println("Fixed XML written to: " + out);
                } else {
                    System.out.println("Fixed XML:\n" + fixedContent);
                }
            }
        }
    }

    private void formatXml(String in, String out) {
        Xml_Rearder reader = new Xml_Rearder(in);
        reader.readFile(in);
        Map<Integer, String> xmlMap = reader.getLinesMap();

        String formatted = FormatingFile.formatToString(xmlMap);
        writeToFile(out, formatted);
        System.out.println("Formatted XML written to: " + out);
    }

    private void convertToJson(String in, String out) {
        Xml_Rearder reader = new Xml_Rearder(in);
        reader.readFile(in);
        String xmlContent = Xml_Rearder.mapToString(reader.getLinesMap());

        try {
            XmlToJsonConverter converter = new XmlToJsonConverter();
            String json = converter.convert(xmlContent);
            writeToFile(out, json);
            System.out.println("JSON written to: " + out);
        } catch (Exception e) {
            error("Conversion failed: " + e.getMessage());
        }
    }

    private void minifyXml(String in, String out) {
        Xml_Rearder reader = new Xml_Rearder(in);
        reader.readFile(in);
        Map<Integer, String> xmlMap = reader.getLinesMap();

        String minified = XmlMinifier.minifyToString(xmlMap);
        writeToFile(out, minified);
        System.out.println("Minified XML written to: " + out);
    }

    private void compressFile(String in, String out) {
        compressFileInternal(in, out, false);
    }

    private void compressJsonFile(String in, String out) {
        compressFileInternal(in, out, true);
    }

    private void compressFileInternal(String in, String out, boolean isJson) {
        java.io.File outFile = new java.io.File(out);
        String outputPath = outFile.getAbsolutePath();
        
        Compression compression = new Compression();
        compression.setOutputPath(outputPath);
        boolean success = isJson 
            ? compression.compressJSON_tokenization(in) 
            : compression.compressXML(in);

        String type = isJson ? "JSON" : "XML";
        String keyFileName = isJson ? "KeyFileJSON.comp" : "KeyFileXML.comp";
        String keyFilePath = outFile.getParent() != null 
            ? new java.io.File(outFile.getParent(), keyFileName).getAbsolutePath()
            : keyFileName;

        if (success) {
            System.out.println(type + " Compression successful!");
            System.out.println("Compressed file: " + outputPath);
            System.out.println("Key file: " + keyFilePath);
        } else {
            error(type + " Compression failed. Check that the file path is valid and you have write permissions.");
        }
    }

    private void decompressFile(String in, String out) {
        decompressFileInternal(in, out, false);
    }

    private void decompressJsonFile(String in, String out) {
        decompressFileInternal(in, out, true);
    }

    private void decompressFileInternal(String in, String out, boolean isJson) {
        String keyFileName = isJson ? "KeyFileJSON.comp" : "KeyFileXML.comp";
        String type = isJson ? "JSON" : "XML";
        java.io.File inFile = new java.io.File(in);
        String keyFilePath;
        java.io.File keyFileInSameDir = new java.io.File(inFile.getParent(), keyFileName);
        
        if (keyFileInSameDir.exists()) {
            keyFilePath = keyFileInSameDir.getAbsolutePath();
        } else if (new java.io.File(keyFileName).exists()) {
            keyFilePath = keyFileName;
        } else {
            error(type + " Decompression failed: Key file not found.\nSearched:\n  1. " + keyFileInSameDir.getAbsolutePath() + "\n  2. " + new java.io.File(keyFileName).getAbsolutePath());
            return;
        }
        
        try {
            XMLDecompressor decompressor = new XMLDecompressor();
            decompressor.loadKeyFile(keyFilePath);
            String compressed = decompressor.readFile(in);
            String decompressed = isJson 
                ? decompressor.decompressJSON(compressed) 
                : decompressor.decompress(compressed);
            decompressor.writeFile(out, decompressed);
            System.out.println(type + " Decompression successful!");
            System.out.println("Output written to: " + out);
            System.out.println("Key file used: " + keyFilePath);
        } catch (IOException e) {
            error(type + " Decompression failed: " + e.getMessage());
        }
    }

    private void writeToFile(String path, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(content);
        } catch (IOException e) {
            error("Failed to write file: " + e.getMessage());
        }
    }
    
    
    private Graph buildGraphFromFile(String inputFile) {
        try {
            SocialNetworkLoader loader = new SocialNetworkLoader();
            List<User> users = loader.loadFromFile(inputFile);
            if (users.isEmpty()) {
                error("No users found in XML file.");
                return null;
            }
            GraphBuilder builder = new GraphBuilder();
            return builder.buildGraph(users);
        } catch (IOException e) {
            error("Failed to load file: " + e.getMessage());
            return null;
        }
    }
    
    private void drawGraph(String inputFile, String outputFile) {
        System.out.println("Building network graph...");
        Graph graph = buildGraphFromFile(inputFile);
        if (graph == null) return;
        
        ArrayList<Pair<String, String>> edges = graph.getEdges();
        String outputPath = (outputFile != null) ? outputFile : "Graphs/graph.png";
        String extension = "png";
        if (outputPath.contains(".")) {
            extension = outputPath.substring(outputPath.lastIndexOf(".") + 1).toLowerCase();
        }
        java.io.File outFile = new java.io.File(outputPath);
        if (outFile.getParentFile() != null && !outFile.getParentFile().exists()) {
            outFile.getParentFile().mkdirs();
        }
        Graph2Photo photo = new Graph2Photo(edges);
        if (outputFile != null) {
            photo.generateToPath(outputPath, extension);
        } else {
            photo.Graph2Photoprint(extension);
        }
        
        System.out.println("Graph generated successfully.");
        System.out.println("Output: " + outputPath);
    }
    
    private void mostActive(String inputFile) {
        Graph graph = buildGraphFromFile(inputFile);
        if (graph == null) return;
        
        NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(graph);
        List<Integer> activeIndices = analyzer.mostActiveUsers();
        
        if (activeIndices.isEmpty()) {
            System.out.println("No active users found.");
        } else {
            System.out.println("Most active user(s):");
            List<Vertex> vertices = graph.getVertices();
            for (int idx : activeIndices) {
                if (idx >= 0 && idx < vertices.size()) {
                    User user = vertices.get(idx).getUser();
                    System.out.println("  " + user.getName() + " (ID: " + user.getId() + ")");
                }
            }
        }
    }
    
    private void mostInfluencer(String inputFile) {
        Graph graph = buildGraphFromFile(inputFile);
        if (graph == null) return;
        
        NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(graph);
        String result = analyzer.mostInfluencerUser();
        System.out.println(result);
    }
    
    private void mutualUsers(String inputFile, String idsStr) {
        Graph graph = buildGraphFromFile(inputFile);
        if (graph == null) return;
        String[] parts = idsStr.split(",");
        List<Integer> ids = new ArrayList<>();
        for (String p : parts) {
            try {
                ids.add(Integer.parseInt(p.trim()));
            } catch (NumberFormatException e) {
                error("Invalid ID: " + p);
            }
        }
        
        NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(graph);
        String result = analyzer.mutualFollowers(ids);
        System.out.println("Mutual followers for IDs: " + idsStr);
        System.out.println(result);
    }
    
    private void suggestUsers(String inputFile, String userIdStr) {
        Graph graph = buildGraphFromFile(inputFile);
        if (graph == null) return;
        
        int userId;
        try {
            userId = Integer.parseInt(userIdStr.trim());
        } catch (NumberFormatException e) {
            error("Invalid user ID: " + userIdStr);
            return;
        }
        
        NetworkAnalyzer_2 analyzer = new NetworkAnalyzer_2(graph);
        List<Integer> suggestions = analyzer.suggestUsers(userId);
        
        if (suggestions.isEmpty()) {
            System.out.println("No suggestions for user ID: " + userId);
        } else {
            System.out.println("Suggested users for ID " + userId + ":");
            List<Vertex> vertices = graph.getVertices();
            for (int idx : suggestions) {
                if (idx >= 0 && idx < vertices.size()) {
                    User user = vertices.get(idx).getUser();
                    System.out.println("  " + user.getName() + " (ID: " + user.getId() + ")");
                }
            }
        }
    }
    
    private void searchByWord(String inputFile, String word) {
        try {
            SocialNetworkLoader loader = new SocialNetworkLoader();
            List<User> users = loader.loadFromFile(inputFile);
            
            System.out.println("Searching for posts containing: " + word);
            boolean found = false;
            for (User user : users) {
                List<Post> posts = user.getPosts();
                if (posts != null) {
                    for (Post post : posts) {
                        String text = post.getText();
                        if (text != null && text.toLowerCase().contains(word.toLowerCase())) {
                            System.out.println("  User: " + user.getName() + " (ID: " + user.getId() + ")");
                            System.out.println("  Post: " + text);
                            System.out.println();
                            found = true;
                        }
                    }
                }
            }
            if (!found) {
                System.out.println("No posts found containing: " + word);
            }
        } catch (IOException e) {
            error("Failed to load file: " + e.getMessage());
        }
    }
    
    private void searchByTopic(String inputFile, String topicSearch) {
        try {
            SocialNetworkLoader loader = new SocialNetworkLoader();
            List<User> users = loader.loadFromFile(inputFile);
            
            System.out.println("Searching for posts with topic: " + topicSearch);
            boolean found = false;
            for (User user : users) {
                List<Post> posts = user.getPosts();
                if (posts != null) {
                    for (Post post : posts) {
                        List<String> topics = post.getTopics();
                        if (topics != null) {
                            for (String t : topics) {
                                if (t.toLowerCase().contains(topicSearch.toLowerCase())) {
                                    System.out.println("  User: " + user.getName() + " (ID: " + user.getId() + ")");
                                    System.out.println("  Topic: " + t);
                                    System.out.println("  Post: " + post.getText());
                                    System.out.println();
                                    found = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (!found) {
                System.out.println("No posts found with topic: " + topicSearch);
            }
        } catch (IOException e) {
            error("Failed to load file: " + e.getMessage());
        }
    }
}
