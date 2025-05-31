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
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.*;

/**
 * Controller class for handling quiz interactions for student users.
 */
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

    @FXML private Label noQuizLabel;
    @FXML private VBox quizDisplayBox;
    @FXML private Button nextButton;
    @FXML private Button restartButton;
    @FXML private Button backButton;
    @FXML private Label userInfoLabel;
    @FXML private User currentUser;
    @FXML private Label hintLabel;
    @FXML private Label selectQuizLabel;

    /**
     * Initializes the controller and sets up quiz list view.
     */
    public void initialize() {
        quizzes = QuizDAO.getAllQuizzes();
        radioButtons = List.of(button1, button2, button3, button4);
        boolean hasQuizzes = !quizzes.isEmpty();
        noQuizLabel.setVisible(!hasQuizzes);
        noQuizLabel.setManaged(!hasQuizzes);
        quizListView.setVisible(hasQuizzes);
        quizListView.setManaged(hasQuizzes);
        selectQuizLabel.setVisible(hasQuizzes);
        selectQuizLabel.setManaged(hasQuizzes);
        if (hasQuizzes) {
            for (Quiz quiz : quizzes) {
                quizListView.getItems().add(quiz.getName());
            }
            quizListView.getSelectionModel().selectFirst();
            Platform.runLater(() -> quizListView.requestFocus());
            onQuizSelected(null);
        }
    }

    /**
     * Displays the current question with answer options.
     */
    private void showQuestion() {
        if (currentIndex < questions.size()) {
            Question q = questions.get(currentIndex);
            quiz.setText(currentQuiz.getName());
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

    /**
     * Triggered when the next button is clicked. Validates answer and progresses quiz.
     */
    @FXML
    protected void onNextButtonClick() throws IOException {
        boolean answerSelected = radioButtons.stream().anyMatch(RadioButton::isSelected);
        if (!answerSelected) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Answer Required");
            alert.setHeaderText("Please select at least one answer before proceeding.");
            alert.setContentText("You must choose an answer to continue to the next question.");
            alert.showAndWait();
            return;
        }
        checkAnswer();
        if (currentIndex < questions.size() - 1) {
            currentIndex++;
            showQuestion();
            nextButton.setDisable(false);
        } else {
            showScore();
            nextButton.setDisable(true);
            question.setText("Quiz finished!");
            restartButton.setVisible(true);
            restartButton.setManaged(true);
        }
    }

    /**
     * Triggered when a quiz is selected from the list.
     */
    @FXML
    private void onQuizSelected(MouseEvent event) {
        int index = quizListView.getSelectionModel().getSelectedIndex();
        if (index >= 0 && index < quizzes.size()) {
            currentQuiz = quizzes.get(index);
            this.questions = currentQuiz.getQuestions();
            this.score = 0;
            this.currentIndex = 0;
            restartButton.setVisible(false);
            restartButton.setManaged(false);
            if (questions != null && !questions.isEmpty()) {
                quizDisplayBox.setVisible(true);
                quizDisplayBox.setManaged(true);
                nextButton.setDisable(false);
                showQuestion();
            } else {
                quizDisplayBox.setVisible(false);
                quizDisplayBox.setManaged(false);
                nextButton.setDisable(true);
            }
        } else {
            quizDisplayBox.setVisible(false);
            quizDisplayBox.setManaged(false);
            nextButton.setDisable(true);
        }
    }

    /**
     * Shows the final score in an alert.
     */
    private void showScore() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Quiz Complete");
        alert.setHeaderText("Your Score");
        alert.setContentText("You scored " + score + " out of " + questions.size());
        alert.showAndWait();
    }

    /**
     * Checks if the selected answer(s) are correct.
     */
    private void checkAnswer() {
        Question q = questions.get(currentIndex);
        List<Integer> correct = q.getCorrectIndexes();
        List<Integer> selected = new ArrayList<>();

        if (button1.isSelected()) selected.add(0);
        if (button2.isSelected()) selected.add(1);
        if (button3.isSelected()) selected.add(2);
        if (button4.isSelected()) selected.add(3);

        Collections.sort(selected);
        List<Integer> sortedCorrect = new ArrayList<>(correct);
        Collections.sort(sortedCorrect);

        if (selected.equals(sortedCorrect)) {
            score++;
        }
    }

    /**
     * Navigates back to the dashboard screen.
     */
    @FXML
    private void onDashboardClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/main.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            MainController mainController = loader.getController();
            mainController.setUser(currentUser);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load main view", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open Dashboard page");
            alert.setContentText("There was a problem loading the requested page. Please try again later.");
            alert.showAndWait();
            throw e;
        }
    }

    /**
     * Navigates to the AI interaction screen.
     */
    @FXML
    private void onAIClick(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cab302ai_teacher/AI.fxml"));
            Scene scene = new Scene(loader.load(), 640, 480);
            AIController aiController = loader.getController();
            aiController.setUser(currentUser);
            String stylesheet = Objects.requireNonNull(Main.class.getResource("style.css")).toExternalForm();
            scene.getStylesheets().add(stylesheet);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to load AI view", e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Could not open AI page");
            alert.setContentText("There was a problem loading the requested page. Please try again later.");
            alert.showAndWait();
            throw e;
        }
    }

    /**
     * Sets the current logged-in user and updates UI.
     */
    @FXML
    public void setUser(User user) {
        this.currentUser = user;
        if (userInfoLabel != null && user != null) {
            userInfoLabel.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
        }
    }

    /**
     * Resets the quiz for a new attempt.
     */
    public void onRestartQuizClick(ActionEvent actionEvent) {
        currentIndex = 0;
        score = 0;
        showQuestion();
        restartButton.setVisible(false);
        restartButton.setManaged(false);
        nextButton.setDisable(false);
    }
}
