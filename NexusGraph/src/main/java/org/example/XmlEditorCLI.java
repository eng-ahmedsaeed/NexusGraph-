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

                default:
                    error("Unknown option: " + arg);
            }
        }

        if (inputFile == null) {
            error("Input file is required. Use -i <file>");
        }

        // ------------------------
        // Execute command
        // ------------------------
        switch (command) {

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

            default:
                error("Unknown command: " + command);
        }
    }

    private void printUsage() {
        System.out.println("\nUsage examples:");
        System.out.println("  xml_editor verify -i input.xml [-f] [-o output.xml]");
        System.out.println("  xml_editor format -i input.xml -o output.xml");
        System.out.println("  xml_editor json -i input.xml -o output.json");
        System.out.println("  xml_editor mini -i input.xml -o output.xml");
        System.out.println("  xml_editor compress -i input.xml -o output.comp");
        System.out.println("  xml_editor decompress -i input.comp -o output.xml\n");
    }

    private void error(String msg) {
        System.err.println("Error: " + msg);
        System.exit(1);
    }

    private void requireOutput(String out) {
        if (out == null) error("Output file is required. Use -o <file>");
    }

    // -------------
    // STUB FUNCTIONS
    // -------------
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
        System.out.println("[MINI] in=" + in + " out=" + out);
    }

    private void compressFile(String in, String out) {
        System.out.println("[COMPRESS] in=" + in + " out=" + out);
    }

    private void decompressFile(String in, String out) {
        System.out.println("[DECOMPRESS] in=" + in + " out=" + out);
    }
}
