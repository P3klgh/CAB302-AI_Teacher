package com.cab302ai_teacher.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
     * This method is called when the login button is clicked.
     * It retrieves the user input and switches to the main scene.
     */
    @FXML
    public void onLoginClick() {
        // Get user input from the text fields
        String email = emailField.getText();
        String password = passwordField.getText();
        System.out.println("Login attempt: " + email + " / " + password);

        // TODO: Add actual authentication logic here later
        // Temporarily transition to the main scene without validation
        try {
            // Load the main scene from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Get the current stage from the email field and set the new scene
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Print the error if the scene fails to load
            e.printStackTrace();
        }
    }
}
