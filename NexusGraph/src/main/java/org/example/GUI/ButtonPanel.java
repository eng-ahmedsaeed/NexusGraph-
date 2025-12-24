package org.example.GUI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class ButtonPanel extends HBox {

    
    public interface ActionHandler {
        void onVerify();
        void onFix();
        void onFormat();
        void onConvertToJson();
        void onMinify();
        void onCompress();
        void onDecompress();
        void onCompressJson();
        void onDecompressJson();
        void onSaveOutput();
    }
    private ActionHandler actionHandler;
    private static final String BTN_PRIMARY = "-fx-font-size: 12px; -fx-padding: 8 16; " +
        "-fx-background-color: #2563EB; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-cursor: hand;";
    private static final String BTN_PRIMARY_HOVER = "-fx-font-size: 12px; -fx-padding: 8 16; " +
        "-fx-background-color: #1D4ED8; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-cursor: hand;";
    private static final String BTN_SECONDARY = "-fx-font-size: 12px; -fx-padding: 8 14; " +
        "-fx-background-color: #E2E8F0; -fx-text-fill: #374151; " +
        "-fx-background-radius: 6; -fx-cursor: hand;";
    private static final String BTN_SECONDARY_HOVER = "-fx-font-size: 12px; -fx-padding: 8 14; " +
        "-fx-background-color: #CBD5E1; -fx-text-fill: #374151; " +
        "-fx-background-radius: 6; -fx-cursor: hand;";
    private static final String BTN_SUCCESS = "-fx-font-size: 12px; -fx-padding: 8 16; " +
        "-fx-background-color: #10B981; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-cursor: hand;";
    private static final String BTN_SUCCESS_HOVER = "-fx-font-size: 12px; -fx-padding: 8 16; " +
        "-fx-background-color: #059669; -fx-text-fill: white; " +
        "-fx-background-radius: 6; -fx-cursor: hand;";

    
    public ButtonPanel(ActionHandler actionHandler) {
        this.actionHandler = actionHandler;
        this.setSpacing(8);
        this.setPadding(new Insets(12, 16, 12, 16));
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #D1D5DB; " +
                     "-fx-border-width: 1 0 0 0;");
        Button verifyBtn = createPrimaryButton("Verify XML", () -> actionHandler.onVerify());
        Button fixBtn = createPrimaryButton("Fix XML", () -> actionHandler.onFix());
        Button formatBtn = createSecondaryButton("Format XML", () -> actionHandler.onFormat());
        Button jsonBtn = createSecondaryButton("To JSON", () -> actionHandler.onConvertToJson());
        Button minifyBtn = createSecondaryButton("Minify", () -> actionHandler.onMinify());
        Button compressBtn = createSecondaryButton("Compress XML", () -> actionHandler.onCompress());
        Button decompressBtn = createSecondaryButton("Decompress XML", () -> actionHandler.onDecompress());
        Button compressJsonBtn = createSecondaryButton("Compress JSON", () -> actionHandler.onCompressJson());
        Button decompressJsonBtn = createSecondaryButton("Decompress JSON", () -> actionHandler.onDecompressJson());
        Button saveBtn = createSuccessButton("Save Output", () -> actionHandler.onSaveOutput());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        this.getChildren().addAll(
            verifyBtn, fixBtn, formatBtn, jsonBtn, 
            minifyBtn, compressBtn, decompressBtn,
            compressJsonBtn, decompressJsonBtn,
            spacer, saveBtn
        );
    }

    
    private Button createPrimaryButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(BTN_PRIMARY);
        button.setOnMouseEntered(e -> button.setStyle(BTN_PRIMARY_HOVER));
        button.setOnMouseExited(e -> button.setStyle(BTN_PRIMARY));
        button.setOnAction(e -> action.run());
        return button;
    }

    
    private Button createSecondaryButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(BTN_SECONDARY);
        button.setOnMouseEntered(e -> button.setStyle(BTN_SECONDARY_HOVER));
        button.setOnMouseExited(e -> button.setStyle(BTN_SECONDARY));
        button.setOnAction(e -> action.run());
        return button;
    }

    
    private Button createSuccessButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setStyle(BTN_SUCCESS);
        button.setOnMouseEntered(e -> button.setStyle(BTN_SUCCESS_HOVER));
        button.setOnMouseExited(e -> button.setStyle(BTN_SUCCESS));
        button.setOnAction(e -> action.run());
        return button;
    }
}
