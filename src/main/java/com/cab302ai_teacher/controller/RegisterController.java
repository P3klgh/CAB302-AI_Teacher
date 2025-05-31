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

/**
 * Controller for the registration screen.
 * Handles user input validation and user registration process.
 */
public class RegisterController {

    /**
     * TextField for user's first name.
     */
    @FXML
    private TextField firstNameField;

    /**
     * TextField for user's last name.
     */
    @FXML
    private TextField lastNameField;

    /**
     * TextField for user's email.
     */
    @FXML
    private TextField emailField;

    /**
     * PasswordField for user's password.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * ComboBox for selecting the user role (Teacher/Student).
     */
    @FXML
    private ComboBox<String> roleComboBox; // Add this in FXML!

    /**
     * Initializes the registration form.
     * Populates the role combo box with role options.
     */
    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Teacher", "Student");
    }

    /**
     * Called when the register button is clicked.
     * Validates the input and attempts to register the user.
     */
    @FXML
    public void onRegisterClick() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleComboBox.getValue();

        if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank() || role == null || role.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Please fill in all fields.");
            return;
        }

        if (!Validator.isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid email format.");
            return;
        }

        if (!Validator.isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Password must be at least 6 characters long, contain a mix of letters, digits, and special characters.");
            return;
        }

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

    /**
     * Called when the back button is clicked.
     * Navigates the user back to the login screen.
     */
    @FXML
    private void onBackToLoginClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while switching screens: " + e.getMessage());
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Navigation error", e);
        }
    }
}
