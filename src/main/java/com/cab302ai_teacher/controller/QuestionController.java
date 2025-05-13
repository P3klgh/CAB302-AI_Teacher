package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import com.cab302ai_teacher.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.*;

public class QuestionController {

    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private User currentUser;

    @FXML
    private Label userInfoLabel;


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
    private void onAIClick(ActionEvent event) throws IOException {
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
        } catch (IOException e) {
            // Log the error
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load AI view", e);

            // Show user-friendly error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open AI page");
            alert.setContentText("There was a problem loading the requested page. Please try again later.");
            alert.showAndWait();

            // Rethrow if needed (or handle it here completely)
            throw e;
        } catch (Exception e) {
            // Handle other unexpected exceptions
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unexpected error", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Application Error");
            alert.setHeaderText("An unexpected error occurred");
            alert.setContentText("Sorry for the inconvenience. The application encountered an unexpected error.");
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

    @FXML
    protected void onNextButtonClick() throws IOException {
        Stage stage = (Stage) nextButton.getScene().getWindow();
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
//        stage.setScene(scene);
    }

    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) nextButton.getScene().getWindow();
        stage.close();
    }
}
