package  org.example;

import java.io.*;
import java.util.Map;
import java.util.Stack;
import java.io.File;
public class FormatingFile {
    private Stack<String> tagNamesStack = new Stack<>();
    private int numberOfTabs;

    private BufferedWriter formated;

    FormatingFile() {
        numberOfTabs = 0;
        String folderName = "output_folder";
        String fileName = "formated_File.xml";
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder,fileName);
        try {
            formated = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void foramtXml(Map<Integer, String> fileData) {


        for (Integer lineNumber : fileData.keySet()) {
            String line = fileData.get(lineNumber).trim();
            if(lineNumber == 0 ||lineNumber== fileData.size()-1){
                try {
                    formated.write(line);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(line.isEmpty())
                continue;
            if (line.startsWith("</")) {
                numberOfTabs--;
                reprintFormatedXml(numberOfTabs, line);
                if (!tagNamesStack.isEmpty()) {
                    tagNamesStack.pop();
                }
            }
            else if (line.startsWith("<")) {
                if (line.contains("</")) {
                    reprintFormatedXml(numberOfTabs, line);
                }
                else {
                    reprintFormatedXml(numberOfTabs, line);
                    String tagName = line.substring(1, line.indexOf('>'));
                    tagNamesStack.push(tagName);
                    numberOfTabs++;
                }
            }
            else {
                reprintFormatedXml(numberOfTabs, line);
            }
        }
        try {
            formated.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void reprintFormatedXml(int indentation, String data) {
        try {
            for (int i = 0; i < indentation; i++) {
                formated.write("    "); 
            }
            formated.write(data);
            formated.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}


