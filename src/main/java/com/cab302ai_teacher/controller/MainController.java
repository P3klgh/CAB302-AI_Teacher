package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import com.cab302ai_teacher.db.DatabaseManager;
import com.cab302ai_teacher.util.PasswordHasher;
import com.cab302ai_teacher.util.Validator;
import com.cab302ai_teacher.db.UserDAO;
import com.cab302ai_teacher.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MainController {
    @FXML
    private User currentUser;

    @FXML
    private Label userInfoLabel;

    @FXML
    private TextField userFirstName;

    @FXML
    private TextField userLastName;

    @FXML
    private TextField userEmail;

    @FXML
    private Button editBtn;

    @FXML
    private Button confirmBtn;


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
            // Log the error
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load login view", e);

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to login screen. Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    private void onQuizzesClick(ActionEvent event) {
        try {
            FXMLLoader loader;
            Scene scene;

            if (Objects.equals(currentUser.getRole(), "Student")) {
                loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/quizzes.fxml"));
                scene = new Scene(loader.load(), 640, 480);

                // Get the correct controller and pass the user
                QuizzesController controller = loader.getController();
                controller.setUser(currentUser);
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/question.fxml"));
                scene = new Scene(loader.load(), 640, 480);

                // Get the correct controller and pass the user
                QuestionController controller = loader.getController();
                controller.setUser(currentUser);
            }

            // Add CSS stylesheet to Scene
            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load quizzes view", e);

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to quizzes screen. Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    private void onAIClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/AI.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);


            // Pass user to other controllers
            AIController aiController = loader.getController();
            aiController.setUser(currentUser);

            // Add CSS stylesheet to Scene
            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load AI view", e);

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to AI screen. Please try again.");
            alert.showAndWait();
        }

    }

    @FXML
    public void setUser(User user) {
        this.currentUser = user;
        if (userInfoLabel != null && user != null) {
            userInfoLabel.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
        }
        userFirstName.setText(currentUser.getFirstName());
        userLastName.setText(currentUser.getLastName());
        userEmail.setText(currentUser.getEmail());
    }

    @FXML
    public void onDetailsEditClick(ActionEvent event) {

        // Checks if one textfield is disabled and uses it as a basis for the remaining textfields
        boolean toggled = userFirstName.isDisable();

        // Reverses the state of the textfields (enables/disables on appropriate click)
        userFirstName.setDisable(!toggled);
        userLastName.setDisable(!toggled);
        userEmail.setDisable(!toggled);

        // Makes confirm button visible/invisible and allows it to take up space
        confirmBtn.setVisible(toggled);
        confirmBtn.setManaged(toggled);

        // Adjusts the edit button's text based on state
        if (toggled) {
            editBtn.setText("Cancel");

        } else { editBtn.setText("Edit"); }

    }

    @FXML
    public void onDetailsConfirmClick(ActionEvent event) {

        // shorthand for textfield values
        String editFirstName = userFirstName.getText();
        String editLastName = userLastName.getText();
        String editEmail = userEmail.getText();

        // each statement checks relative validity of the textfield input (and if it's been changed)
        if (!editFirstName.equals(currentUser.getFirstName()) && editFirstName != null && !editFirstName.trim().isEmpty()) {
            System.out.println(editFirstName);
            dbDetailsUpdate("firstName", editFirstName);
        }

        if (!editLastName.equals(currentUser.getLastName()) && editLastName != null && !editLastName.trim().isEmpty()) {
            System.out.println(editLastName);
            dbDetailsUpdate("lastName", editLastName);
        }

        if (!editEmail.equals(currentUser.getEmail())) {
            if (Validator.isValidEmail(editEmail)) {
                System.out.println(editEmail);
                dbDetailsUpdate("email", editEmail);
            }
        }

        // Updates session info to match updated user details
        User updatedUser = UserDAO.getUserByEmail(editEmail);
        this.currentUser = updatedUser;
        userInfoLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " (" + currentUser.getRole() + ")");

    }

    public void dbDetailsUpdate(String column, String detail) {
        String query = "UPDATE users SET " + column + " = ? WHERE email = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, detail);
            stmt.setString(2, currentUser.getEmail());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Login query failed: " + e.getMessage());
        }
    }
}

