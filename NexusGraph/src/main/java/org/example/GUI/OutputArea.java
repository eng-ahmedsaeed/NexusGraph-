package org.example.GUI;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

public class OutputArea extends VBox {
    private TextArea textArea;

    
    public OutputArea() {
        this.setSpacing(8);
        this.setPadding(new Insets(12));
        this.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 4;");
        Label label = new Label("Output");
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2563EB;");
        textArea = new TextArea();
        textArea.setPromptText("Output will appear here...");
        textArea.setEditable(false);  // Make it read-only
        textArea.setWrapText(true);
        textArea.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; " +
                         "-fx-font-size: 12px; " +
                         "-fx-control-inner-background: #FAFAFA; " +
                         "-fx-border-color: #D1D5DB; " +
                         "-fx-border-radius: 4; " +
                         "-fx-background-radius: 4; " +
                         "-fx-padding: 8;");
        VBox.setVgrow(textArea, javafx.scene.layout.Priority.ALWAYS);
        this.getChildren().addAll(label, textArea);
    }

    
    public void setText(String text) {
        textArea.setText(text);
    }

    
    public void clear() {
        textArea.clear();
    }

    
    public String getText() {
        return textArea.getText();
    }
}
