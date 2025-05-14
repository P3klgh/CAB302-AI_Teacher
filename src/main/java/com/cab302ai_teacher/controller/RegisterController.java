package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.db.UserDAO;
import com.cab302ai_teacher.util.Validator;
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
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox; // Add this in FXML!

    @FXML
    public void initialize() {
        // Populate roles in ComboBox
        roleComboBox.getItems().addAll("Teacher", "Student");
    }

    @FXML
    public void onRegisterClick() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        // Ensure all fields are filled in
        if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank() || role == null || role.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
            return;
        }

        // Validate email format using a regex pattern
        if (!Validator.isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid email format.");
            return;
        }

        // Validate password strength
        if (!Validator.isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Password must be at least 6 characters long, contain a mix of letters, digits, and special characters.");
            return;
        }

        // Proceed with user registration
        if (UserDAO.registerUser(firstName, lastName, email, password, role)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
            try {
                Stage stage = (Stage) emailField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
                stage.setScene(new Scene(loader.load(), 640, 480));
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "An error occurred while switching screens: " + e.getMessage());
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