package com.cab302ai_teacher.controller;

import com.cab302ai_teacher.Main;
import com.cab302ai_teacher.db.QuizDAO;
import com.cab302ai_teacher.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.*;

/**
 * Controller responsible for managing quizzes and questions.
 * Teachers can view, edit, delete, and add quizzes and their questions.
 * This class also handles navigation and user interface updates.
 */
public class QuestionController {

    /**
     * The currently logged-in user.
     */
    @FXML
    private User currentUser;

    /**
     * Label showing logged-in user info.
     */
    @FXML
    private Label userInfoLabel;

    /**
     * The VBox container where questions are displayed.
     */
    @FXML
    private VBox questionCtnr;

    /**
     * ListView that shows all quizzes.
     */
    @FXML
    private ListView<Quiz> quizListView;

    /**
     * Observable list of quizzes for UI.
     */
    private ObservableList<Quiz> quizObservableList = FXCollections.observableArrayList();

    private List<Question> questions;
    private Quiz currentQuiz;
    private List<Quiz> quizzes;
    private TextField quizNameField;

    /**
     * List of questions marked for deletion.
     */
    private final List<Question> deletedQuestions = new ArrayList<>();

    /**
     * Initializes the controller and loads all quizzes from the database.
     */
    public void initialize() {
        quizzes = QuizDAO.getAllQuizzes();
        quizObservableList.setAll(quizzes);
        quizListView.setItems(quizObservableList);

        quizListView.setCellFactory(listView -> new ListCell<Quiz>() {
            private final HBox cellContainer = new HBox(10);
            private final Label nameLabel = new Label();
            private final Region spacer = new Region();
            private final Button deleteButton = new Button("Delete Quiz");

            {
                HBox.setHgrow(spacer, Priority.ALWAYS);
                deleteButton.getStyleClass().add("btn-primary");

                deleteButton.setOnAction(e -> {
                    Quiz quiz = getItem();
                    if (quiz != null) {
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirm Deletion");
                        confirm.setHeaderText("Delete Quiz");
                        confirm.setContentText("Are you sure you want to delete \"" + quiz.getName() + "\"?");

                        confirm.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    QuizDAO.deleteQuiz(quiz.getId());
                                    quizzes.remove(quiz);
                                    quizObservableList.remove(quiz);

                                    if (quiz.equals(currentQuiz)) {
                                        currentQuiz = null;
                                        questionCtnr.getChildren().clear();
                                    }
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                    showAlert("Database Error", "Could not delete quiz from database.");
                                }
                            }
                        });
                    }
                });

                cellContainer.getChildren().addAll(nameLabel, spacer, deleteButton);
            }

            @Override
            protected void updateItem(Quiz quiz, boolean empty) {
                super.updateItem(quiz, empty);
                if (empty || quiz == null) {
                    setGraphic(null);
                } else {
                    nameLabel.setText(quiz.getName());
                    setGraphic(cellContainer);
                }
            }
        });

        if (!quizzes.isEmpty()) {
            quizListView.getSelectionModel().selectFirst();
            onQuizSelected(null);
        }
    }

    /**
     * Handles selection of a quiz from the ListView.
     *
     * @param mouseEvent the selection event
     */
    public void onQuizSelected(MouseEvent mouseEvent) {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            currentQuiz = selectedQuiz;
            loadQuiz(selectedQuiz);
        } else {
            System.out.println("⚠️ No quiz selected.");
        }
    }

    /**
     * Loads selected quiz and displays its questions in the UI.
     *
     * @param currentQuiz the selected quiz
     */
    public void loadQuiz(Quiz currentQuiz) {
        questionCtnr.getChildren().clear();

        Label quizLabel = new Label("Quiz Name:");
        quizNameField = new TextField(currentQuiz.getName());
        VBox quizNameBox = new VBox(5, quizLabel, quizNameField);
        questionCtnr.getChildren().add(quizNameBox);

        List<Question> questions = currentQuiz.getQuestions();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            VBox questionBox = new VBox(5);
            Label questionLabel = new Label("Question " + (i + 1));
            TextField questionField = new TextField(question.getQuestion());
            questionBox.getChildren().addAll(questionLabel, questionField);

            List<String> options = question.getOptions();
            List<Integer> correctIndexes = question.getCorrectIndexes();

            for (int j = 0; j < options.size(); j++) {
                HBox optionBox = new HBox(10);
                RadioButton radioButton = new RadioButton();
                TextField optionField = new TextField(options.get(j));
                optionField.setPromptText("Option " + (j + 1));

                if (correctIndexes.contains(j)) {
                    radioButton.setSelected(true);
                }

                optionBox.getChildren().addAll(radioButton, optionField);
                questionBox.getChildren().add(optionBox);
            }

            Button deleteBtn = new Button("Delete Question");
            deleteBtn.getStyleClass().add("btn-primary");
            deleteBtn.setOnAction(event -> handleDeleteQuestion(question, questionBox));
            questionBox.getChildren().add(deleteBtn);
            questionCtnr.getChildren().add(questionBox);
        }
    }

    /**
     * Navigates to the dashboard screen.
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
            showAlert("Navigation Error", "Unable to navigate to dashboard screen. Please try again.");
        }
    }

    /**
     * Navigates to the AI screen.
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
            showAlert("Navigation Error", "Could not open AI page.");
            throw e;
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Unexpected error", e);
            showAlert("Application Error", "An unexpected error occurred.");
        }
    }

    /**
     * Sets the current user and updates UI.
     *
     * @param user the logged-in user
     */
    @FXML
    public void setUser(User user) {
        this.currentUser = user;
        if (userInfoLabel != null && user != null) {
            userInfoLabel.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
        }
    }

    /**
     * Cancels edits and reloads the current quiz.
     */
    public void onCancel(ActionEvent actionEvent) {
        if (currentQuiz != null) {
            loadQuiz(currentQuiz);
        }
    }

    /**
     * Confirms and saves changes to the quiz and its questions.
     */
    @FXML
    public void onEditConfirm(ActionEvent actionEvent) throws SQLException {
        int index = quizListView.getSelectionModel().getSelectedIndex();

        if (index >= 0 && index < quizzes.size()) {
            if (questionCtnr.getChildren().isEmpty()) {
                showAlert("No Quiz Loaded", "Please select a quiz before confirming edits.");
                return;
            }

            Node quizBox = questionCtnr.getChildren().get(0);
            if (quizBox instanceof VBox) {
                VBox quizNameBox = (VBox) quizBox;
                for (Node node : quizNameBox.getChildren()) {
                    if (node instanceof TextField) {
                        String updatedName = ((TextField) node).getText();
                        currentQuiz.setQuizName(updatedName);
                    }
                }
            }

            List<Question> quizQuestions = currentQuiz.getQuestions();
            for (int i = 1; i < questionCtnr.getChildren().size(); i++) {
                Node node = questionCtnr.getChildren().get(i);
                if (!(node instanceof VBox)) continue;

                VBox questionBox = (VBox) node;
                if (i - 1 >= quizQuestions.size()) {
                    quizQuestions.add(new Question("", new ArrayList<>(), new ArrayList<>(), -1));
                }
                Question question = quizQuestions.get(i - 1);
                TextField questionField = (TextField) questionBox.getChildren().get(1);
                question.setQuestion(questionField.getText());

                List<String> updatedOptions = new ArrayList<>();
                List<Integer> correctIndexes = new ArrayList<>();

                for (int j = 2; j < questionBox.getChildren().size(); j++) {
                    Node optionNode = questionBox.getChildren().get(j);
                    if (!(optionNode instanceof HBox)) continue;

                    HBox optionBox = (HBox) optionNode;
                    if (optionBox.getChildren().size() < 2) continue;

                    RadioButton radioButton = (RadioButton) optionBox.getChildren().get(0);
                    TextField optionField = (TextField) optionBox.getChildren().get(1);

                    updatedOptions.add(optionField.getText());

                    if (radioButton.isSelected()) {
                        correctIndexes.add(j - 2);
                    }
                }

                if (correctIndexes.isEmpty()) {
                    showAlert("Validation Error", "Each question must have at least one correct answer selected.\n\nMissing in: Question " + i);
                    return;
                }

                question.setOptions(updatedOptions);
                question.setCorrectIndexes(correctIndexes);
            }

            QuizDAO.updateQuiz(currentQuiz, deletedQuestions);
            quizListView.getItems().set(index, currentQuiz);
            quizListView.getSelectionModel().select(currentQuiz);

        } else {
            showAlert("Quiz Selection Error", "Please select a quiz to confirm edits.");
        }
    }

    /**
     * Removes a question from the quiz and UI.
     */
    private void handleDeleteQuestion(Question question, VBox questionBox) {
        currentQuiz.removeQuestion(question);
        if (question.getId() != -1) {
            deletedQuestions.add(question);
        }
        questionCtnr.getChildren().remove(questionBox);
    }

    /**
     * Adds a new quiz and loads it for editing.
     */
    @FXML
    public void onAddQuiz(ActionEvent actionEvent) {
        Quiz newQuiz = new Quiz("New Quiz", new ArrayList<>());
        try {
            int newId = QuizDAO.insertQuiz(newQuiz);
            newQuiz.setId(newId);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Could not save new quiz to the database.");
            return;
        }

        quizzes.add(newQuiz);
        quizObservableList.add(newQuiz);
        quizListView.getSelectionModel().select(newQuiz);
        currentQuiz = newQuiz;
        loadQuiz(currentQuiz);
    }

    /**
     * Adds a new question to the current quiz in the UI.
     */
    @FXML
    public void onAddQuestion(ActionEvent event) {
        if (currentQuiz == null) {
            System.err.println("No quiz selected.");
            return;
        }

        VBox questionBox = new VBox();
        questionBox.setSpacing(10);

        Label label = new Label("New Question");
        TextField questionField = new TextField();
        questionField.setPromptText("Enter question text");
        questionBox.getChildren().addAll(label, questionField);

        for (int i = 0; i < 4; i++) {
            HBox optionBox = new HBox(10);
            RadioButton rb = new RadioButton();
            TextField optField = new TextField();
            optField.setPromptText("Option " + (i + 1));
            optionBox.getChildren().addAll(rb, optField);
            questionBox.getChildren().add(optionBox);
        }

        Button deleteBtn = new Button("Delete Question");
        deleteBtn.setOnAction(e -> handleDeleteQuestion(new Question("", Arrays.asList("", "", "", ""), new ArrayList<>(), -1), questionBox));
        questionBox.getChildren().add(deleteBtn);
        questionCtnr.getChildren().add(questionBox);
        currentQuiz.addQuestion(new Question("", Arrays.asList("", "", "", ""), new ArrayList<>(), -1));
    }

    /**
     * Shows an error alert dialog.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
