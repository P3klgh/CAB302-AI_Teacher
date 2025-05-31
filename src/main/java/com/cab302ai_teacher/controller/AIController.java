package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import com.cab302ai_teacher.model.User;
import com.cab302ai_teacher.db.ErrorHandler;
import com.cab302ai_teacher.util.APIHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Objects;
import java.util.logging.*;

/**
 * AIController handles the AI chat screen where users can interact with a chatbot.
 * It also manages navigation and user data display.
 */
public class AIController {

    /**
     * API key for OpenAI (replace with your actual key).
     */
    private static final String OPENAI_API_KEY = "YOUR_OPENAI_API_KEY";  // Replace with your OpenAI API Key

    /**
     * The endpoint URL for OpenAI API requests.
     */
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    /**
     * Text area displaying the conversation between user and chatbot.
     */
    @FXML
    private TextArea chatArea;

    /**
     * Label showing the logged-in user's info.
     */
    @FXML
    private Label userInfoLabel;

    /**
     * Field where the user types their message.
     */
    @FXML
    private TextField userInput;

    /**
     * Button to send the message.
     */
    @FXML
    private Button sendButton;

    /**
     * Loading spinner shown while waiting for a bot response.
     */
    @FXML
    private ProgressIndicator progressIndicator;

    /**
     * The currently logged-in user.
     */
    @FXML
    private User currentUser;

    /**
     * Called when the Send button is clicked.
     * Sends the user's message to the chatbot and displays the response.
     */
    public void onSendMessageClick() {
        String userMessage = userInput.getText().trim();

        if (userMessage.isEmpty()) {
            ErrorHandler.showAlert(Alert.AlertType.WARNING, "Please enter a message.");
            return;
        }

        // Disable input while loading
        userInput.setDisable(true);
        sendButton.setDisable(true);
        progressIndicator.setVisible(true);
        progressIndicator.setManaged(true);

        // Display user's message
        chatArea.appendText("You: " + userMessage + "\n");

        // Call OpenAI API asynchronously
        APIHandler.getBotResponse(userMessage).thenAccept(botResponse -> {
            Platform.runLater(() -> {
                chatArea.appendText("Bot: " + botResponse + "\n");
                chatArea.setScrollTop(Double.MAX_VALUE);
                userInput.clear();
                progressIndicator.setVisible(false);
                progressIndicator.setManaged(false);
                userInput.setDisable(false);
                sendButton.setDisable(false);
            });
        });
    }

    /**
     * Shows an alert message in the UI.
     *
     * @param message the message to show in the alert dialog
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Navigates back to the dashboard (main) screen.
     *
     * @param event the action event triggered by the dashboard button
     */
    @FXML
    private void onDashboardClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            MainController mainController = loader.getController();
            mainController.setUser(currentUser);

            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            Logger.getLogger(AIController.class.getName()).log(Level.SEVERE, "Failed to load dashboard view", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to dashboard screen. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Navigates to the quizzes screen depending on the user's role.
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
            Logger.getLogger(AIController.class.getName()).log(Level.SEVERE, "Failed to load quizzes view", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to quizzes screen. Please try again.");
            alert.showAndWait();
        }
    }

    /**
     * Sets the current user and updates the user information label.
     *
     * @param user the currently logged-in user
     */
    @FXML
    public void setUser(User user) {
        this.currentUser = user;
        if (userInfoLabel != null && user != null) {
            userInfoLabel.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
        }
    }
}
