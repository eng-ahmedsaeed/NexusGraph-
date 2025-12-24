package  org.example;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
public class XmlMinifier {
    public static String minifyMapToXML(Map<Integer, String> codeLines) {
        try {
            String folderName = "output_folder";
            String fileName = "minified_File.xml";
            File folder = new File(folderName);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, fileName);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Map.Entry<Integer, String> entry : codeLines.entrySet()) {
                    String code = entry.getValue();
                    code = code.trim();
                    writer.write(code);
                }
            }
            System.out.println("XML folder created at: \n" + folder.getAbsolutePath());
            return file.getAbsolutePath();
        } catch (IOException e) {
            System.out.println("Error: Could not create or write to the file!\n");
            return null;
        }
    }
}
