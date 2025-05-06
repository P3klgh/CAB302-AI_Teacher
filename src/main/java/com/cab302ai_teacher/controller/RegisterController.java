package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.db.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.cab302ai_teacher.db.ErrorHandler.showAlert;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox; // Add this in FXML!

    @FXML
    public void initialize() {
        // Populate roles in ComboBox
        roleComboBox.getItems().addAll("admin", "teacher", "student");
    }

    @FXML
    public void onRegisterClick() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (email.isBlank() || password.isBlank() || role == null) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields including role.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Invalid email format.");
            return;
        }

        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Password must be at least 6 characters.");
            return;
        }

        // Optional: hash the password
        // replace with a real hasher if available

        if (UserDAO.registerUser(email, password, role)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
            try {
                Stage stage = (Stage) emailField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
                stage.setScene(new Scene(loader.load(), 640, 480));
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "An error occurred while switching screens: " + e.getMessage());
                // Optional: Log the full stack trace to a file
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Navigation error", e);
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration failed. Email may already exist.");
        }
    }

    @FXML
    private void onBackToLoginClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while switching screens: " + e.getMessage());
            // Optional: Log the full stack trace to a file
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Navigation error", e);
        }
    }


}