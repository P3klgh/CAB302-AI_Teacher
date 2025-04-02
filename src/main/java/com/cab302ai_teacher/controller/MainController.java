package com.cab302ai_teacher.controller;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class MainController {

    /**
     * This method is called when the logout button is clicked.
     * Currently, it only prints a message to the console.
     *
     * TODO: Implement logic to return to the login screen if needed.
     * For example: get the current stage and load login.fxml again.
     */
    @FXML
    private void onLogoutClick() {
        System.out.println("Logout button clicked");

        // TODO: Add logic to return to the login screen if necessary
        // Example: Get the current stage and load login.fxml
    }
}
