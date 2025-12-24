package  org.example;
import java.nio.file.Path;

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

        // Level 2 parameters
        String word = null;
        String topic = null;
        String ids = null;
        String userId = null;

        // ------------------------
        // Parse flags and arguments
        // ------------------------
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

        // ------------------------
        // Validate input file
        // ------------------------
        if (inputFile == null) {
            error("Input file is required. Use -i <file>");
        } else {
            inputFile = Path.of(inputFile).toAbsolutePath().toString();
        }

        // ------------------------
        // Execute command
        // ------------------------
        switch (command) {

            // -------- Level 1 --------
            case "verify":
                verifyXml(inputFile, outputFile, fix);
                break;

            case "format":
                requireOutput(outputFile);
                formatXml(inputFile, outputFile);
                break;

            case "json":
                requireOutput(outputFile);
                convertToJson(inputFile, outputFile);
                break;

            case "mini":
                requireOutput(outputFile);
                minifyXml(inputFile, outputFile);
                break;

            case "compress":
                requireOutput(outputFile);
                compressFile(inputFile, outputFile);
                break;

            case "decompress":
                requireOutput(outputFile);
                decompressFile(inputFile, outputFile);
                break;

            // -------- Level 2 : Network Analysis --------
            case "most_active":
                mostActive(inputFile);
                break;

            case "most_influencer":
                mostInfluencer(inputFile);
                break;

            case "mutual":
                if (ids == null)
                    error("mutual requires -ids <id1,id2,...>");
                mutualUsers(inputFile, ids);
                break;

            case "suggest":
                if (userId == null)
                    error("suggest requires -id <userId>");
                suggestUsers(inputFile, userId);
                break;

            // -------- Level 2 : Post Search --------
            case "search":
                if (word != null && topic != null)
                    error("Use either -w or -t, not both");

                if (word != null)
                    searchByWord(inputFile, word);
                else if (topic != null)
                    searchByTopic(inputFile, topic);
                else
                    error("search requires -w <word> or -t <topic>");
                break;

            default:
                error("Unknown command: " + command);
        }
    }

    // ------------------------
    // Helper methods
    // ------------------------
    private void printUsage() {
        System.out.println("\nUsage examples:");
        System.out.println("  xml_editor verify -i input.xml [-f] [-o output.xml]");
        System.out.println("  xml_editor format -i input.xml -o output.xml");
        System.out.println("  xml_editor json -i input.xml -o output.json");
        System.out.println("  xml_editor mini -i input.xml -o output.xml");
        System.out.println("  xml_editor compress -i input.xml -o output.comp");
        System.out.println("  xml_editor decompress -i input.comp -o output.xml");
        System.out.println("  xml_editor most_active -i input.xml");
        System.out.println("  xml_editor most_influencer -i input.xml");
        System.out.println("  xml_editor mutual -i input.xml -ids 1,2,3");
        System.out.println("  xml_editor suggest -i input.xml -id 1");
        System.out.println("  xml_editor search -w word -i input.xml");
        System.out.println("  xml_editor search -t topic -i input.xml\n");
    }

    private void error(String msg) {
        System.err.println("Error: " + msg);
        System.exit(1);
    }

    private void requireOutput(String out) {
        if (out == null)
            error("Output file is required. Use -o <file>");
    }

    // ------------------------
    // Level 1 stub functions
    // ------------------------
    private void verifyXml(String in, String out, boolean fix) {
        System.out.println("[VERIFY] in=" + in + " out=" + out + " fix=" + fix);
    }

    private void formatXml(String in, String out) {
        System.out.println("[FORMAT] in=" + in + " out=" + out);
    }

    private void convertToJson(String in, String out) {
        System.out.println("[JSON] in=" + in + " out=" + out);
    }

    private void minifyXml(String in, String out) {
        System.out.println("[MINIFY] in=" + in + " out=" + out);
    }

    private void compressFile(String in, String out) {
        System.out.println("[COMPRESS] in=" + in + " out=" + out);
    }

    private void decompressFile(String in, String out) {
        System.out.println("[DECOMPRESS] in=" + in + " out=" + out);
    }

    // ------------------------
    // Level 2 stub functions
    // ------------------------
    private void mostActive(String in) {
        System.out.println("[MOST ACTIVE] in=" + in);
    }

    private void mostInfluencer(String in) {
        System.out.println("[MOST INFLUENCER] in=" + in);
    }

    private void mutualUsers(String in, String ids) {
        System.out.println("[MUTUAL USERS] in=" + in + " ids=" + ids);
    }

    private void suggestUsers(String in, String userId) {
        System.out.println("[SUGGEST USERS] in=" + in + " id=" + userId);
    }

    private void searchByWord(String in, String word) {
        System.out.println("[SEARCH WORD] in=" + in + " word=" + word);
    }

    private void searchByTopic(String in, String topic) {
        System.out.println("[SEARCH TOPIC] in=" + in + " topic=" + topic);
    }
}
