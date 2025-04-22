package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.db.UserDAO;
import com.cab302ai_teacher.util.PasswordHasher;
import com.cab302ai_teacher.util.Validator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void onRegisterClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in both fields.");
            return;
        }

        if (!Validator.isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid email format.");
            return;
        }

        if (!Validator.isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING,
                    "Password must be at least 8 characters and include an uppercase letter, " +
                            "a lowercase letter, a digit, and a special character.");
            return;
        }

        String hashedPassword = PasswordHasher.hashPassword(password);

        if (UserDAO.registerUser(email, hashedPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
            navigateToLogin();
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration failed. Email may already exist.");
        }
    }

    @FXML
    private void onBackToLoginClick() {
        navigateToLogin();
    }

    private void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error navigating to login page. Please try again.");
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Navigation error", e);
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}