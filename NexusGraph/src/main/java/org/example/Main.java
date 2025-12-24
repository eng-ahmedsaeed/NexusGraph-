package org.example;

import org.example.GUI.GuiApplication;
import org.example.Level_1.XmlEditorCLI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0) {
            XmlEditorCLI cli = new XmlEditorCLI();
            cli.run(args);
        } else {
            runInteractiveShell();
        }
    }
    
    private static void runInteractiveShell() {
        Scanner scanner = new Scanner(System.in);
        XmlEditorCLI cli = new XmlEditorCLI();
        
        System.out.println("â•”â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•—");
        System.out.println("â•‘           XML Editor / NexusGraph - Interactive Mode     â•‘");
        System.out.println("â• â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•£");
        System.out.println("â•‘  Commands:                                               â•‘");
        System.out.println("â•‘    xml_editor          - Launch GUI                      â•‘");
        System.out.println("â•‘    verify -i file.xml  - Validate XML                    â•‘");
        System.out.println("â•‘    format -i file.xml  - Format XML                      â•‘");
        System.out.println("â•‘    draw -i file.xml    - Generate graph                  â•‘");
        System.out.println("â•‘    help                - Show all commands               â•‘");
        System.out.println("â•‘    exit                - Exit the program                â•‘");
        System.out.println("â•šâ•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•â•");
        System.out.println();
        
        while (true) {
            System.out.print("xml_editor> ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                continue;
            }
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            
            if (input.equalsIgnoreCase("xml_editor") || input.equalsIgnoreCase("gui")) {
                System.out.println("Launching GUI...");
                GuiApplication.main(new String[]{});
                continue;
            }
            
            if (input.equalsIgnoreCase("help")) {
                printHelp();
                continue;
            }
            String[] cmdArgs = input.split("\\s+");
            try {
                cli.run(cmdArgs);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        
        scanner.close();
    }
    
    private static void printHelp() {
        System.out.println("\n=== XML Editor Commands ===\n");
        System.out.println("LEVEL 1 - XML Processing:");
        System.out.println("  verify -i <file.xml> [-f] [-o <output.xml>]  Validate XML, -f to fix");
        System.out.println("  format -i <file.xml> -o <output.xml>         Pretty-print XML");
        System.out.println("  mini -i <file.xml> -o <output.xml>           Minify XML");
        System.out.println("  json -i <file.xml> -o <output.json>          Convert to JSON");
        System.out.println("  compress -i <file.xml> -o <output.comp>      Compress XML");
        System.out.println("  decompress -i <file.comp> -o <output.xml>    Decompress XML");
        System.out.println();
        System.out.println("LEVEL 2 - Social Network Analysis:");
        System.out.println("  draw -i <file.xml> [-o <output.png>]         Generate network graph");
        System.out.println("  most_active -i <file.xml>                    Find most active users");
        System.out.println("  most_influencer -i <file.xml>                Find most influential users");
        System.out.println("  mutual -i <file.xml> -ids <1,2,3>            Find mutual followers");
        System.out.println("  suggest -i <file.xml> -id <user_id>          Get user suggestions");
        System.out.println("  search -i <file.xml> -w <word>               Search posts by word");
        System.out.println("  search -i <file.xml> -t <topic>              Search posts by topic");
        System.out.println();
        System.out.println("OTHER:");
        System.out.println("  xml_editor / gui                             Launch GUI");
        System.out.println("  help                                         Show this help");
        System.out.println("  exit / quit                                  Exit program");
        System.out.println();
    }
}
