package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import com.cab302ai_teacher.model.*;
import com.cab302ai_teacher.db.QuizDAO;
import com.cab302ai_teacher.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.*;

public class QuizzesController {

    private List<Question> questions;
    private int score = 0;
    private int currentIndex = 0;
    private final ToggleGroup singleChoiceGroup = new ToggleGroup();
    private List<RadioButton> radioButtons;

    @FXML private Label question;
    @FXML private Label questionIndex;
    @FXML private Label quiz;
    @FXML private RadioButton button1, button2, button3, button4;
    @FXML private ListView<String> quizListView;
    private List<Quiz> quizzes;
    private Quiz currentQuiz;

    @FXML
    private Button nextButton;

    @FXML
    private Button backButton;

    @FXML
    private Label userInfoLabel;

    @FXML
    private User currentUser;

    @FXML
    private Label hintLabel;

    public void initialize() {
        quizzes = QuizDAO.getAllQuizzes();
        for (Quiz quiz : quizzes) {
            quizListView.getItems().add(quiz.getName().toString());
        }
        radioButtons = List.of(button1, button2, button3, button4);
        if (!quizzes.isEmpty()) {
            quizListView.getSelectionModel().selectFirst();
            Platform.runLater(() -> quizListView.requestFocus());
            onQuizSelected(null);
        }
    }

    private void showQuestion() {
        if (currentIndex < questions.size()) {
            Question q = questions.get(currentIndex);
            quiz.setText(currentQuiz.getName().toString());
            questionIndex.setText("Question " + (currentIndex + 1));
            question.setText(q.getQuestion());

            boolean isMultipleAnswer = q.getCorrectIndexes().size() > 1;
            hintLabel.setText(isMultipleAnswer ? "Select all that apply:" : "Select one answer:");

            List<String> options = q.getOptions();

            for (int i = 0; i < radioButtons.size(); i++) {
                RadioButton rb = radioButtons.get(i);

                if (i < options.size()) {
                    rb.setText(options.get(i));
                    rb.setVisible(true);
                    rb.setSelected(false);

                    rb.setToggleGroup(null);
                    if (!isMultipleAnswer) {
                        rb.setToggleGroup(singleChoiceGroup);
                    }
                } else {
                    rb.setVisible(false);
                    rb.setText("");
                    rb.setSelected(false);
                    rb.setToggleGroup(null);
                }
            }
        }
    }

    @FXML
    protected void onNextButtonClick() throws IOException {
        checkAnswer();
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            showQuestion();
            nextButton.setDisable(false);
        } else {
            showScore();
            nextButton.setDisable(true);
            question.setText("Quiz finished!");
        }
    }

    @FXML
    private void onQuizSelected(MouseEvent event) {
        int index = quizListView.getSelectionModel().getSelectedIndex();
        nextButton.setDisable(false);
        currentQuiz = quizzes.get(index);
        this.questions = currentQuiz.getQuestions();
        this.score = 0;
        this.currentIndex = 0;
        showQuestion();
    }

    private void showScore() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Complete");
        alert.setHeaderText("Your Score");
        alert.setContentText("You scored " + score + " out of " + questions.size());
        alert.showAndWait();
    }

    private void checkAnswer() {
        Question q = questions.get(currentIndex);
        List<Integer> correct = q.getCorrectIndexes();
        List<Integer> selected = new ArrayList<>();

        if (button1.isSelected()) selected.add(0);
        if (button2.isSelected()) selected.add(1);
        if (button3.isSelected()) selected.add(2);
        if (button4.isSelected()) selected.add(3);


        // Sort lists
        Collections.sort(selected);
        List<Integer> sortedCorrect = new ArrayList<>(correct);
        Collections.sort(sortedCorrect);

        if (selected.equals(sortedCorrect)) {
            score++;
        }
    }

    @FXML
    private void onDashboardClick(ActionEvent event) throws IOException {
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
        } catch (IOException e) {
            // Log the error
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load main view", e);

            // Show user-friendly error message
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open Dashboard page");
            alert.setContentText("There was a problem loading the requested page. Please try again later.");
            alert.showAndWait();

            // Rethrow to respect the method's throws declaration
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
}
