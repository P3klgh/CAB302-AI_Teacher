package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.db.UserDAO;
import com.cab302ai_teacher.util.PasswordHasher;
import com.cab302ai_teacher.util.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

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

        if (email.isBlank() || password.isBlank()) {
            showAlert("Please enter both email and password.");
            return;
        }

        if (!Validator.isValidEmail(email)) {
            showAlert("Invalid email format.");
            return;
        }

        String hashedPassword = PasswordHasher.hashPassword(password);

        if (!UserDAO.isValidUser(email, hashedPassword)) {
            showAlert("Incorrect email or password.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load main application screen", e);
            showAlert("Failed to load main application screen.");

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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load registration screen", e);
            showAlert("Failed to load registration screen.");
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
