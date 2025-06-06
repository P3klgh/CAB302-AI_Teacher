package com.cab302ai_teacher.db;

import javafx.scene.control.Alert;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for displaying alert dialogs and logging errors.
 */
public class ErrorHandler {

    /**
     * Logger for recording error messages.
     */
    private static final Logger logger = Logger.getLogger(ErrorHandler.class.getName());

    /**
     * Shows an alert dialog with the specified type and message.
     * Logs errors if the type is ERROR.
     *
     * @param type The alert type (e.g., ERROR, WARNING, INFORMATION)
     * @param message The message to display
     */
    public static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        if (type == Alert.AlertType.ERROR) {
            logger.log(Level.SEVERE, message);
        }
    }

    /**
     * Shows an alert dialog with the specified type, title, and message.
     * Logs errors if the type is ERROR.
     *
     * @param type The alert type (e.g., ERROR, WARNING, INFORMATION)
     * @param title The alert window title
     * @param message The message to display
     */
    public static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

        if (type == Alert.AlertType.ERROR) {
            logger.log(Level.SEVERE, title + ": " + message);
        }
    }
}
