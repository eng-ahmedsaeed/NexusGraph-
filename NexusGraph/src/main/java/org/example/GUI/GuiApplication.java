package org.example.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuiApplication extends Application {

    
    @Override
    public void start(Stage primaryStage) {
        EditorWindow editorWindow = new EditorWindow(primaryStage);
        Scene scene = new Scene(editorWindow.getRoot(), 1100, 750);
        primaryStage.setTitle("XML Editor");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
}
