package com.cab302ai_teacher.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

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
}
