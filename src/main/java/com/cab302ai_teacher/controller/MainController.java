package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.util.Objects;
import java.util.logging.*;


public class MainController {

    /**
     * Called when the logout button is clicked.
     * Switches the scene back to the login screen.
     */
    @FXML
    private void onLogoutClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // 현재 Stage를 얻고 login.fxml로 전환
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load login view", e);

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to login screen. Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    private void onQuizzesClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/quizzes.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Add CSS stylesheet to Scene
            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load quizzes view", e);

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to quizzes screen. Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    private void onAIClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/AI.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Add CSS stylesheet to Scene
            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load AI view", e);

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to AI screen. Please try again.");
            alert.showAndWait();
        }

    }
}
