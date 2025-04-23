package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class LoginController {

    // Reference to the email input field in the login form
    @FXML
    private TextField emailField;

    // Reference to the password input field in the login form
    @FXML
    private PasswordField passwordField;

    /**
     * Called when the login button is clicked.
     * Checks the inputs and transitions to the main scene.
     */
    @FXML
    public void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        System.out.println("Login attempt: " + email + " / " + password);


        if (email.isBlank() || password.isBlank()) {
            showAlert("Please enter both email and password.");
            return;
        }


        if (!email.contains("@") || !email.contains(".")) {
            showAlert("Invalid email format.");
            return;
        }



        try {
            // Load main.fxml and transition to main scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Add css stylesheet to scene
            String stylesheet = Main.class.getResource("style.css").toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the register button is clicked.
     * Navigates to the registration screen.
     */
    @FXML
    public void onRegisterClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/register.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a warning alert with the provided message.
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
