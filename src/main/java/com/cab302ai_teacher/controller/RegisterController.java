package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.db.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

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


        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.WARNING, "Invalid email format.");
            return;
        }


        if (password.length() < 6) {
            showAlert(Alert.AlertType.WARNING, "Password must be at least 6 characters.");
            return;
        }

        // ✅ 정상 입력 시 등록 시도
        if (UserDAO.registerUser(email, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Registration successful!");
            try {
                Stage stage = (Stage) emailField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
                stage.setScene(new Scene(loader.load(), 640, 480));
            } catch (Exception e) {
                e.printStackTrace();
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
            e.printStackTrace();
        }
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
