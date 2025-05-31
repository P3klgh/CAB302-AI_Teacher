package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import com.cab302ai_teacher.db.UserDAO;
import com.cab302ai_teacher.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.logging.*;

/**
 * Controller class responsible for handling the login screen.
 * It manages user authentication and navigation to other scenes.
 */
public class LoginController {

    /**
     * Reference to the email input field in the login form.
     */
    @FXML
    private TextField emailField;

    /**
     * Reference to the password input field in the login form.
     */
    @FXML
    private PasswordField passwordField;

    /**
     * Called when the login button is clicked.
     * Validates user input and, if successful, transitions to the main screen.
     */
    @FXML
    public void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        System.out.println("Login attempt: " + email);

        if (email.isBlank() || password.isBlank()) {
            showAlert("Please enter both email and password.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showAlert("Invalid email format.");
            return;
        }

        if (!UserDAO.isValidUser(email, password)) {
            showAlert("Invalid email or password.");
            return;
        }

        // Successful login: load main view
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Pass user data to MainController
            MainController mainController = loader.getController();
            User loggedInUser = UserDAO.getUserByEmail(email);
            mainController.setUser(loggedInUser);

            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Failed to load main view", e);
            showAlert("Failed to load main view.");
        }
    }

    /**
     * Called when the register button is clicked.
     * Navigates the user to the registration screen.
     */
    @FXML
    public void onRegisterClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/register.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, "Failed to load registration view", e);
            showAlert("Failed to load registration screen. Please try again.");
        }
    }

    /**
     * Shows a warning alert dialog with the given message.
     *
     * @param message the text to display in the alert dialog
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
