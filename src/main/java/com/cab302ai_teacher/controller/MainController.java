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
import javafx.scene.layout.VBox;
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

/**
 * MainController handles the main user dashboard after login.
 * It allows users to view and update their profile,
 * navigate to other screens (quizzes, AI, etc.), and log out.
 */
public class MainController {

    /**
     * The user currently logged into the system.
     */
    @FXML
    private User currentUser;

    /**
     * Label used to show basic user information at the top of the screen.
     */
    @FXML
    private Label userInfoLabel;

    /**
     * TextField for the user's first name.
     */
    @FXML
    private TextField userFirstName;

    /**
     * TextField for the user's last name.
     */
    @FXML
    private TextField userLastName;

    /**
     * TextField for the user's email address.
     */
    @FXML
    private TextField userEmail;

    /**
     * Button that toggles edit mode.
     */
    @FXML
    private Button editBtn;

    /**
     * Button used to confirm updates to user details.
     */
    @FXML
    private Button confirmBtn;

    /**
     * A VBox container that holds editable fields for the user's details.
     */
    @FXML
    private VBox userEditBox;

    /**
     * Logs the user out and navigates back to the login screen.
     *
     * @param event the action event triggered by the logout button
     */
    @FXML
    private void onLogoutClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/login.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load login view", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to login screen. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Navigates to the quizzes screen depending on the user role.
     * Students see a quizzes list; teachers see a question editor.
     *
     * @param event the action event triggered by the quizzes button
     */
    @FXML
    private void onQuizzesClick(ActionEvent event) {
        try {
            FXMLLoader loader;
            Scene scene;

            if (Objects.equals(currentUser.getRole(), "student")) {
                loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/quizzes.fxml"));
                scene = new Scene(loader.load(), 640, 480);

                QuizzesController controller = loader.getController();
                controller.setUser(currentUser);
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/question.fxml"));
                scene = new Scene(loader.load(), 640, 480);

                QuestionController controller = loader.getController();
                controller.setUser(currentUser);
            }

            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load quizzes view", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to quizzes screen. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Navigates to the AI view screen.
     *
     * @param event the action event triggered by the AI button
     */
    @FXML
    private void onAIClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/AI.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            AIController aiController = loader.getController();
            aiController.setUser(currentUser);

            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, "Failed to load AI view", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to AI screen. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Sets the user for this session and updates the profile view with their details.
     *
     * @param user the currently logged-in user
     */
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

    /**
     * Toggles the visibility and enablement of profile editing fields.
     *
     * @param event the action event triggered by the edit button
     */
    @FXML
    public void onDetailsEditClick(ActionEvent event) {
        boolean toggled = userEditBox.isDisable();
        userEditBox.setDisable(!toggled);
        confirmBtn.setVisible(toggled);
        confirmBtn.setManaged(toggled);
        editBtn.setText(toggled ? "Cancel" : "Edit");
    }

    /**
     * Validates and saves changes to the user's personal information.
     * Updates both the database and session data.
     *
     * @param event the action event triggered by the confirm button
     */
    @FXML
    public void onDetailsConfirmClick(ActionEvent event) {
        String editFirstName = userFirstName.getText();
        String editLastName = userLastName.getText();
        String editEmail = userEmail.getText();

        if (!editFirstName.equals(currentUser.getFirstName()) && editFirstName != null && !editFirstName.trim().isEmpty()) {
            dbDetailsUpdate("firstName", editFirstName);
        }

        if (!editLastName.equals(currentUser.getLastName()) && editLastName != null && !editLastName.trim().isEmpty()) {
            dbDetailsUpdate("lastName", editLastName);
        }

        if (!editEmail.equals(currentUser.getEmail())) {
            if (Validator.isValidEmail(editEmail)) {
                dbDetailsUpdate("email", editEmail);
            }
        }

        User updatedUser = UserDAO.getUserByEmail(editEmail);
        this.currentUser = updatedUser;
        userInfoLabel.setText(currentUser.getFirstName() + " " + currentUser.getLastName() + " (" + currentUser.getRole() + ")");
    }

    /**
     * Updates a specific column in the user's database record.
     *
     * @param column the database column to update
     * @param detail the new value to store in the column
     */
    public void dbDetailsUpdate(String column, String detail) {
        String query = "UPDATE users SET " + column + " = ? WHERE email = ?";

        try (Connection conn = DatabaseManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, detail);
            stmt.setString(2, currentUser.getEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Login query failed: " + e.getMessage());
        }
    }
}
