package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AIController {
    @FXML
    private void onDashboardClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Add css stylesheet to scene
            String stylesheet = Main.class.getResource("style.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load scene", e);

            // Show user-friendly error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the requested page. Please try again.");
            alert.showAndWait();
        }
    }

    @FXML
    private void onQuizzesClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/quizzes.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Add css stylesheet to scene
            String stylesheet = Main.class.getResource("style.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load scene", e);

            // Show user-friendly error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to load the requested page. Please try again.");
            alert.showAndWait();
        }
    }
}