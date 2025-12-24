package org.example.GUI;

import org.example.Level_1.Xml_Rearder;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileBrowser extends HBox {
    private Stage stage;
    private InputArea inputArea;
    private Label filePathLabel;
    private boolean isJsonFile = false;
    private FileTypeListener fileTypeListener;

    
    public interface FileTypeListener {
        void onFileTypeChanged(boolean isJson);
    }

    
    public FileBrowser(Stage stage, InputArea inputArea) {
        this.stage = stage;
        this.inputArea = inputArea;
        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setAlignment(Pos.CENTER_LEFT);
        this.setStyle("-fx-background-color: #e8e8e8; -fx-border-color: #cccccc; " +
                     "-fx-border-width: 0 0 1 0;");
        Button browseButton = new Button("Browse File");
        browseButton.setStyle("-fx-font-size: 13px; -fx-padding: 8 15;");
        browseButton.setOnAction(e -> browseFile());
        filePathLabel = new Label("No file selected");
        filePathLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #666666;");
        this.getChildren().addAll(browseButton, filePathLabel);
    }

    
    public void setFileTypeListener(FileTypeListener listener) {
        this.fileTypeListener = listener;
    }

    
    public boolean isJsonFile() {
        return isJsonFile;
    }

    
    private void browseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML or JSON File");
        FileChooser.ExtensionFilter allFilter = 
            new FileChooser.ExtensionFilter("XML & JSON Files", "*.xml", "*.json");
        FileChooser.ExtensionFilter xmlFilter = 
            new FileChooser.ExtensionFilter("XML Files (*.xml)", "*.xml");
        FileChooser.ExtensionFilter jsonFilter = 
            new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json");
        fileChooser.getExtensionFilters().addAll(allFilter, xmlFilter, jsonFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            String fileName = selectedFile.getName().toLowerCase();
            isJsonFile = fileName.endsWith(".json");
            String fileType = isJsonFile ? " [JSON]" : " [XML]";
            filePathLabel.setText(selectedFile.getAbsolutePath() + fileType);
            filePathLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
            loadFileContent(selectedFile);
            if (fileTypeListener != null) {
                fileTypeListener.onFileTypeChanged(isJsonFile);
            }
        }
    }

    
    private void loadFileContent(File file) {
        String content = Xml_Rearder.readFileToString(file.getAbsolutePath());
        if (content.isEmpty()) {
            inputArea.setText("Error loading file or file is empty.");
        } else {
            inputArea.setText(content);
        }
    }
}
