package org.example.GUI;

import org.example.Level_1.*;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EditorWindow implements ButtonPanel.ActionHandler {
    private BorderPane root;
    private Stage stage;
    private FileBrowser fileBrowser;
    private InputArea inputArea;
    private OutputArea outputArea;
    private ButtonPanel buttonPanel;
    private Level2Panel level2Panel;
    private boolean isJsonFile = false;

    
    public EditorWindow(Stage stage) {
        this.stage = stage;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #F5F7FA;");
        createComponents();
        assembleLayout();
        setupLevel2Callbacks();
    }
    private javafx.scene.control.SplitPane editorSplitPane;
    private javafx.scene.layout.VBox graphViewContainer;
    private javafx.scene.image.ImageView graphImageView;
    private javafx.scene.layout.VBox buildResultsContainer;
    private javafx.scene.control.TextArea buildResultsTextArea;

    
    private void createComponents() {
        inputArea = new InputArea();
        outputArea = new OutputArea();
        fileBrowser = new FileBrowser(stage, inputArea);
        fileBrowser.setFileTypeListener(isJson -> {
            this.isJsonFile = isJson;
            if (isJson) {
                outputArea.setText("JSON file loaded. Only Compress/Decompress operations are available.");
            }
        });
        buttonPanel = new ButtonPanel(this);
        level2Panel = new Level2Panel(stage);
        createGraphViewContainer();
        createBuildResultsContainer();
    }

    
    private void createGraphViewContainer() {
        graphViewContainer = new javafx.scene.layout.VBox(16);
        graphViewContainer.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        graphViewContainer.setPadding(new javafx.geometry.Insets(20));
        graphViewContainer.setStyle("-fx-background-color: #F5F7FA;");
        javafx.scene.control.Button backBtn = new javafx.scene.control.Button("Back to XML Editor");
        backBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-color: #2563EB; -fx-text-fill: white; " +
                        "-fx-background-radius: 6; -fx-padding: 12 24; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backBtn.getStyle().replace("#2563EB", "#1D4ED8")));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backBtn.getStyle().replace("#1D4ED8", "#2563EB")));
        backBtn.setOnAction(e -> showEditorView());
        javafx.scene.control.Label header = new javafx.scene.control.Label("Network Graph Visualization");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E40AF;");
        javafx.scene.layout.StackPane imageContainer = new javafx.scene.layout.StackPane();
        imageContainer.setStyle("-fx-background-color: white; " +
                               "-fx-background-radius: 8; " +
                               "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 10, 0, 0, 2); " +
                               "-fx-padding: 16;");
        graphImageView = new javafx.scene.image.ImageView();
        graphImageView.setPreserveRatio(true);
        graphImageView.setFitHeight(450);
        graphImageView.setFitWidth(600);
        
        imageContainer.getChildren().add(graphImageView);
        javafx.scene.layout.VBox.setVgrow(imageContainer, javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.control.Label infoLabel = new javafx.scene.control.Label("Graph loaded from: Graphs/graph.png");
        infoLabel.setStyle("-fx-text-fill: #64748B; -fx-font-size: 11px;");
        
        graphViewContainer.getChildren().addAll(backBtn, header, imageContainer, infoLabel);
    }

    
    private void createBuildResultsContainer() {
        buildResultsContainer = new javafx.scene.layout.VBox(16);
        buildResultsContainer.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        buildResultsContainer.setPadding(new javafx.geometry.Insets(20));
        buildResultsContainer.setStyle("-fx-background-color: #F5F7FA;");
        javafx.scene.control.Button backBtn = new javafx.scene.control.Button("Back to XML Editor");
        backBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                        "-fx-background-color: #2563EB; -fx-text-fill: white; " +
                        "-fx-background-radius: 6; -fx-padding: 12 24; -fx-cursor: hand;");
        backBtn.setOnMouseEntered(e -> backBtn.setStyle(backBtn.getStyle().replace("#2563EB", "#1D4ED8")));
        backBtn.setOnMouseExited(e -> backBtn.setStyle(backBtn.getStyle().replace("#1D4ED8", "#2563EB")));
        backBtn.setOnAction(e -> showEditorView());
        javafx.scene.control.Label header = new javafx.scene.control.Label("Build Results");
        header.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1E40AF;");
        buildResultsTextArea = new javafx.scene.control.TextArea();
        buildResultsTextArea.setEditable(false);
        buildResultsTextArea.setWrapText(true);
        buildResultsTextArea.setStyle("-fx-font-family: 'Consolas', 'Monaco', monospace; " +
                                     "-fx-font-size: 13px; " +
                                     "-fx-background-color: white; " +
                                     "-fx-border-color: #D1D5DB; " +
                                     "-fx-border-radius: 8; " +
                                     "-fx-background-radius: 8;");
        javafx.scene.layout.VBox.setVgrow(buildResultsTextArea, javafx.scene.layout.Priority.ALWAYS);
        javafx.scene.control.Button showGraphBtn = new javafx.scene.control.Button("Show Network Graph");
        showGraphBtn.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; " +
                             "-fx-background-color: #10B981; -fx-text-fill: white; " +
                             "-fx-background-radius: 6; -fx-padding: 12 24; -fx-cursor: hand;");
        showGraphBtn.setOnMouseEntered(e -> showGraphBtn.setStyle(showGraphBtn.getStyle().replace("#10B981", "#059669")));
        showGraphBtn.setOnMouseExited(e -> showGraphBtn.setStyle(showGraphBtn.getStyle().replace("#059669", "#10B981")));
        showGraphBtn.setOnAction(e -> showGraphView());
        
        buildResultsContainer.getChildren().addAll(backBtn, header, buildResultsTextArea, showGraphBtn);
    }

    
    private void showBuildResultsView(String resultsText) {
        buildResultsTextArea.setText(resultsText);
        buildResultsTextArea.positionCaret(0);
        root.setCenter(buildResultsContainer);
        root.setBottom(null);
    }

    
    private void setupLevel2Callbacks() {
        level2Panel.setOnShowGraph(() -> showGraphView());
        level2Panel.setXmlContentSupplier(() -> inputArea.getText());
        level2Panel.setOnBuildComplete(() -> {
            String results = level2Panel.getResultsText();
            showBuildResultsView(results);
        });
        level2Panel.setCenterResultsUpdater(text -> {
            buildResultsTextArea.setText(text);
            buildResultsTextArea.positionCaret(0);
        });
    }

    
    private void showGraphView() {
        String[] possiblePaths = {
            "Graphs/graph.png",
            "Graphs/graph.jpg",
            "../Graphs/graph.png",
            "../Graphs/graph.jpg"
        };
        
        java.io.File graphFile = null;
        String foundPath = null;
        
        for (String path : possiblePaths) {
            java.io.File file = new java.io.File(path);
            if (file.exists()) {
                graphFile = file;
                foundPath = path;
                break;
            }
        }
        
        if (graphFile != null && graphFile.exists()) {
            try {
                String imageUri = graphFile.toURI().toString();
                javafx.scene.image.Image image = new javafx.scene.image.Image(imageUri);
                graphImageView.setImage(image);
            } catch (Exception e) {
                level2Panel.setResults("Error loading graph image: " + e.getMessage());
                return;
            }
        } else {
            level2Panel.setResults("Graph image not found.\n\n" +
                "Please click 'Build Network Graph' first.\n\n" +
                "Searched locations:\n" +
                "- Graphs/graph.png\n" +
                "- Graphs/graph.jpg");
            return;
        }
        root.setCenter(graphViewContainer);
        root.setBottom(null);
    }

    
    private void showEditorView() {
        root.setCenter(editorSplitPane);
        root.setBottom(buttonPanel);
    }

    
    private void assembleLayout() {
        root.setTop(fileBrowser);
        editorSplitPane = new javafx.scene.control.SplitPane();
        editorSplitPane.getItems().addAll(inputArea, outputArea);
        editorSplitPane.setDividerPositions(0.5);  // 50/50 split
        root.setCenter(editorSplitPane);
        root.setBottom(buttonPanel);
        root.setRight(level2Panel);
    }

    
    public BorderPane getRoot() {
        return root;
    }

    
    @Override
    public void onVerify() {
        if (isJsonFile) {
            outputArea.setText("Error: Verify is only available for XML files.\nFor JSON files, only Compress and Decompress operations are available.");
            return;
        }
        String input = inputArea.getText();
        if (input.isEmpty()) {
            outputArea.setText("Error: No input XML to verify.");
            return;
        }
        try {
            Map<Integer, String> xmlMap = Xml_Rearder.parseString(input);
            XMLValidator validator = new XMLValidator(new HashMap<>(xmlMap));
            validator.validate();
            
            if (validator.errors.isEmpty()) {
                outputArea.setText("XML is valid! No errors found.");
                level2Panel.setXmlValid(true);
                level2Panel.setXmlContent(input);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("XML Validation Errors:\n\n");
                java.util.TreeMap<Integer, String> sortedErrors = new java.util.TreeMap<>(validator.errors);
                for (Map.Entry<Integer, String> entry : sortedErrors.entrySet()) {
                    sb.append("Line ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }
                outputArea.setText(sb.toString());
                level2Panel.setXmlValid(false);
            }
        } catch (Exception e) {
            outputArea.setText("Error during validation: " + e.getMessage());
            level2Panel.setXmlValid(false);
        }
    }

    
    @Override
    public void onFix() {
        if (isJsonFile) {
            outputArea.setText("Error: Fix is only available for XML files.\nFor JSON files, only Compress and Decompress operations are available.");
            return;
        }
        String input = inputArea.getText();
        if (input.isEmpty()) {
            outputArea.setText("Error: No input XML to fix.");
            return;
        }
        try {
            Map<Integer, String> xmlMap = Xml_Rearder.parseString(input);
            XMLValidator validator = new XMLValidator(new HashMap<>(xmlMap));
            validator.validate();
            
            if (validator.errors.isEmpty()) {
                outputArea.setText("XML is already valid. No fixes needed.");
            } else {
                HashMap<Integer, String> fixedXml = validator.applyFixes();
                String fixedContent = Xml_Rearder.mapToString(fixedXml);
                Map<Integer, String> fixedMap = Xml_Rearder.parseString(fixedContent);
                XMLValidator verifyValidator = new XMLValidator(new HashMap<>(fixedMap));
                verifyValidator.validate();
                
                if (verifyValidator.errors.isEmpty()) {
                    inputArea.setText(fixedContent);
                    outputArea.setText(fixedContent);
                } else {
                    inputArea.setText(fixedContent);
                    StringBuilder sb = new StringBuilder();
                    sb.append("XML partially fixed but still has errors:\n\n");
                    sb.append("--- Remaining Errors ---\n");
                    java.util.TreeMap<Integer, String> sortedErrors = new java.util.TreeMap<>(verifyValidator.errors);
                    for (Map.Entry<Integer, String> entry : sortedErrors.entrySet()) {
                        sb.append("Line ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                    }
                    sb.append("\nSome errors may need manual fixing (e.g., missing closing tags for multi-line content).");
                    outputArea.setText(sb.toString());
                }
            }
        } catch (Exception e) {
            outputArea.setText("Error during fixing: " + e.getMessage());
        }
    }

    
    @Override
    public void onFormat() {
        if (isJsonFile) {
            outputArea.setText("Error: Format is only available for XML files.\nFor JSON files, only Compress and Decompress operations are available.");
            return;
        }
        String input = inputArea.getText();
        if (input.isEmpty()) {
            outputArea.setText("Error: No input XML to format.");
            return;
        }
        try {
            Map<Integer, String> xmlMap = Xml_Rearder.parseString(input);
            String formatted = FormatingFile.formatToString(xmlMap);
            outputArea.setText(formatted);
        } catch (Exception e) {
            outputArea.setText("Error during formatting: " + e.getMessage());
        }
    }

    
    @Override
    public void onConvertToJson() {
        if (isJsonFile) {
            outputArea.setText("Error: Convert to JSON is only available for XML files.\nThe file is already JSON format.");
            return;
        }
        String input = inputArea.getText();
        if (input.isEmpty()) {
            outputArea.setText("Error: No input XML to convert.");
            return;
        }
        try {
            XmlToJsonConverter converter = new XmlToJsonConverter();
            String json = converter.convert(input);
            outputArea.setText(json);
        } catch (Exception e) {
            outputArea.setText("Error during conversion: " + e.getMessage());
        }
    }

    
    @Override
    public void onMinify() {
        if (isJsonFile) {
            outputArea.setText("Error: Minify is only available for XML files.\nFor JSON files, only Compress and Decompress operations are available.");
            return;
        }
        String input = inputArea.getText();
        if (input.isEmpty()) {
            outputArea.setText("Error: No input XML to minify.");
            return;
        }
        try {
            Map<Integer, String> xmlMap = Xml_Rearder.parseString(input);
            String minified = XmlMinifier.minifyToString(xmlMap);
            outputArea.setText(minified);
        } catch (Exception e) {
            outputArea.setText("Error during minification: " + e.getMessage());
        }
    }

    
    @Override
    public void onCompress() {
        compressInternal(false);
    }

    
    @Override
    public void onCompressJson() {
        compressInternal(true);
    }

    
    private void compressInternal(boolean isJson) {
        String input = inputArea.getText();
        if (input.isEmpty()) {
            outputArea.setText("Error: No input to compress.");
            return;
        }
        
        String type = isJson ? "JSON" : "XML";
        String defaultFileName = isJson ? "compressed_json.comp" : "compressed_output.comp";
        String tempExtension = isJson ? ".json" : ".xml";
        String keyFileName = isJson ? "KeyFileJSON.comp" : "KeyFileXML.comp";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Compressed " + type + " File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Compressed Files (*.comp)", "*.comp")
        );
        fileChooser.setInitialFileName(defaultFileName);
        
        File outputFile = fileChooser.showSaveDialog(stage);
        if (outputFile == null) {
            outputArea.setText("Compression cancelled.");
            return;
        }
        String keyFilePath = outputFile.getParent() != null 
            ? new File(outputFile.getParent(), keyFileName).getAbsolutePath()
            : keyFileName;
        
        try {
            File tempFile = File.createTempFile("input", tempExtension);
            try (FileWriter writer = new FileWriter(tempFile)) {
                writer.write(input);
            }
            
            Compression compression = new Compression();
            compression.setOutputPath(outputFile.getAbsolutePath());
            boolean success = isJson 
                ? compression.compressJSON_tokenization(tempFile.getAbsolutePath())
                : compression.compressXML(tempFile.getAbsolutePath());
            
            if (success) {
                outputArea.setText(type + " Compression successful!\n\nCompressed file saved to: " + outputFile.getAbsolutePath() + "\nKey file saved to: " + keyFilePath);
            } else {
                outputArea.setText(type + " Compression failed. Check that:\n1. The file path is valid\n2. You have write permissions\n3. The " + type + " is valid");
            }
            tempFile.delete();
        } catch (Exception e) {
            outputArea.setText("Error during " + type + " compression: " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    @Override
    public void onDecompress() {
        decompressInternal(false);
    }

    
    @Override
    public void onDecompressJson() {
        decompressInternal(true);
    }

    
    private void decompressInternal(boolean isJson) {
        String type = isJson ? "JSON" : "XML";
        String keyFileName = isJson ? "KeyFileJSON.comp" : "KeyFileXML.comp";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Compressed " + type + " File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Compressed Files (*.comp)", "*.comp")
        );
        
        File compressedFile = fileChooser.showOpenDialog(stage);
        if (compressedFile == null) {
            outputArea.setText("No file selected for " + type + " decompression.");
            return;
        }
        
        try {
            XMLDecompressor decompressor = new XMLDecompressor();
            File keyFileInSameDir = new File(compressedFile.getParent(), keyFileName);
            File keyFileInCwd = new File(keyFileName);
            String keyFilePath;
            
            if (keyFileInSameDir.exists()) {
                keyFilePath = keyFileInSameDir.getAbsolutePath();
            } else if (keyFileInCwd.exists()) {
                keyFilePath = keyFileInCwd.getAbsolutePath();
            } else {
                outputArea.setText("Error: Key file not found.\nSearched:\n  1. " + keyFileInSameDir.getAbsolutePath() + "\n  2. " + keyFileInCwd.getAbsolutePath());
                return;
            }
            
            decompressor.loadKeyFile(keyFilePath);
            String compressed = decompressor.readFile(compressedFile.getAbsolutePath());
            String decompressed = isJson 
                ? decompressor.decompressJSON(compressed) 
                : decompressor.decompress(compressed);
            outputArea.setText(decompressed);
        } catch (Exception e) {
            outputArea.setText("Error during " + type + " decompression: " + e.getMessage());
        }
    }

    
    @Override
    public void onSaveOutput() {
        String outputText = outputArea.getText();
        if (outputText == null || outputText.isEmpty()) {
            outputArea.setText("Nothing to save. Output is empty.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Output");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml"),
            new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"),
            new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"),
            new FileChooser.ExtensionFilter("All Files (*.*)", "*.*")
        );

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(outputText);
                outputArea.setText("Output saved successfully to:\n" + file.getAbsolutePath());
            } catch (IOException e) {
                outputArea.setText("Error saving file: " + e.getMessage());
            }
        }
    }
}
