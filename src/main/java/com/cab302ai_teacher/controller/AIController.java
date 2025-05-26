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

public class AIController {
    private static final String OPENAI_API_KEY = "YOUR_OPENAI_API_KEY";  // Replace with your OpenAI API Key
    private static final String OPENAI_API_URL = "https://api.openai.com/v1/completions";

    @FXML
    private TextArea chatArea;

    @FXML
    private Label userInfoLabel;
    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    // Handles the click event of the send button
    @FXML
    private ProgressIndicator progressIndicator;  // Add this in your FXML file

    @FXML
    private User currentUser;
    public void onSendMessageClick() {
        String userMessage = userInput.getText().trim();

        if (userMessage.isEmpty()) {
            ErrorHandler.showAlert(Alert.AlertType.WARNING, "Please enter a message.");
            return;
        }

        // Disable the input field and button during the API call
        userInput.setDisable(true);
        sendButton.setDisable(true);
        progressIndicator.setVisible(true);  // Show loading indicator
        progressIndicator.setManaged(true);  // Show loading indicator

        // Display the user's message in the chat area
        chatArea.appendText("You: " + userMessage + "\n");

        // Make the API call asynchronously
        APIHandler.getBotResponse(userMessage).thenAccept(botResponse -> {
            Platform.runLater(() -> {
                chatArea.appendText("Bot: " + botResponse + "\n");
                chatArea.setScrollTop(Double.MAX_VALUE);  // Scroll to the bottom
                userInput.clear();  // Clear the input field
                progressIndicator.setVisible(false);  // Hide loading indicator
                progressIndicator.setManaged(false);  // Hide loading indicator
                userInput.setDisable(false);
                sendButton.setDisable(false);
            });
        });
    }

    // Show alert messages
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onDashboardClick(ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);

            // Pass user to other controllers
            MainController mainController = loader.getController();
            mainController.setUser(currentUser);

            // Add CSS stylesheet to Scene
            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            // Log the error
            Logger.getLogger(AIController.class.getName()).log(Level.SEVERE, "Failed to load dashboard view", e);

            // Inform the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText(null);
            alert.setContentText("Unable to navigate to dashboard screen. Please try again.");
            alert.showAndWait();
        }
    }

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

            // Add CSS stylesheet to Scene
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

    @FXML
    public void setUser(User user) {
        this.currentUser = user;
        if (userInfoLabel != null && user != null) {
            userInfoLabel.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
        }
    }
}
