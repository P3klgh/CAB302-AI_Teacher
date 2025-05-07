package com.cab302ai_teacher;

import com.cab302ai_teacher.db.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * Entry point of the JavaFX application.
 * This class initializes the database and loads the login screen.
 */
public class Main extends Application {

    /**
     * This method is called when the JavaFX application starts.
     * It initializes the database and sets the initial scene to the login screen.
     */
    @Override
    public void start(Stage stage) throws Exception {
        // Initialize the database (create tables if they don't exist)
        DatabaseManager.initializeDatabase();

        // Load the login screen from the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
        Scene scene = new Scene(loader.load(), 640, 480);

        // Add css stylesheet to scene
        String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(stylesheet);

        // Set window title and show the login screen
        stage.setTitle("AI Teacher Assistant");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Main method - launches the JavaFX application.
     *
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}
