package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.security.spec.ECField;

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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    @FXML
    private void onAIClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/AI.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Add css stylesheet to scene
            String stylesheet = Main.class.getResource("style.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
