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

public class QuestionController {

    @FXML
    private User currentUser;

    @FXML
    private Label userInfoLabel;

    @FXML
    private VBox questionCtnr;

    @FXML
    private ListView<Quiz> quizListView;
    private ObservableList<Quiz> quizObservableList = FXCollections.observableArrayList();
    private List<Question> questions;
    private Quiz currentQuiz;
    private List<Quiz> quizzes;
    private TextField quizNameField;
    private final List<Question> deletedQuestions = new ArrayList<>();

    public void initialize() {
        quizzes = QuizDAO.getAllQuizzes();
        quizObservableList.setAll(quizzes);
        quizListView.setItems(quizObservableList);

        quizListView.setCellFactory(listView -> new ListCell<Quiz>() {
            private final HBox cellContainer = new HBox(10); // spacing between elements
            private final Label nameLabel = new Label();
            private final Region spacer = new Region(); // this pushes the delete button to the right
            private final Button deleteButton = new Button("Delete Quiz");

            {
                HBox.setHgrow(spacer, Priority.ALWAYS); // this allows the spacer to take up all available space

                deleteButton.getStyleClass().add("deleteButton"); // optional: CSS styling

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



    public void onQuizSelected(MouseEvent mouseEvent) {
        Quiz selectedQuiz = quizListView.getSelectionModel().getSelectedItem();
        if (selectedQuiz != null) {
            currentQuiz = selectedQuiz;
            loadQuiz(selectedQuiz);
        }else {
            System.out.println("⚠️ No quiz selected.");
        }
    }

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
            deleteBtn.setOnAction(event -> handleDeleteQuestion(question, questionBox));

            questionBox.getChildren().add(deleteBtn);
            questionCtnr.getChildren().add(questionBox);
        }
    }

    @FXML
    private void onDashboardClick(ActionEvent event) {
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

    public void onCancel(ActionEvent actionEvent) {
        if (currentQuiz != null) {
            loadQuiz(currentQuiz);
        }
    }

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
                        currentQuiz.setName(updatedName);
                    }
                }
            }

            List<Question> quizQuestions = currentQuiz.getQuestions();
            for (int i = 1; i < questionCtnr.getChildren().size(); i++) {
                Node node = questionCtnr.getChildren().get(i);
                if (!(node instanceof VBox)) continue;

                VBox questionBox = (VBox) node;
                if (i - 1 >= quizQuestions.size()) {
                    Question newQ = new Question("", new ArrayList<>(), new ArrayList<>(), -1);
                    quizQuestions.add(newQ);
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
                    RadioButton radioButton = (RadioButton) optionBox.getChildren().get(0);
                    TextField optionField = (TextField) optionBox.getChildren().get(1);

                    updatedOptions.add(optionField.getText());

                    if (radioButton.isSelected()) {
                        correctIndexes.add(j - 2);
                    }
                }

                question.setOptions(updatedOptions);
                question.setCorrectIndexes(correctIndexes);
            }

            QuizDAO.updateQuiz(currentQuiz, deletedQuestions);
            quizListView.getItems().set(index, currentQuiz);
            quizListView.getSelectionModel().select(currentQuiz);


        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("Quiz Selection Error");
            alert.setContentText("Please select a quiz to confirm edits.");
            alert.showAndWait();
        }
    }

    private void handleDeleteQuestion(Question question, VBox questionBox) {
        currentQuiz.getQuestions().remove(question);
        if (question.getId() != -1) {
            deletedQuestions.add(question);
        }

        questionCtnr.getChildren().remove(questionBox);
    }

    @FXML
    public void onAddQuiz(ActionEvent actionEvent) {
        Quiz newQuiz = new Quiz("New Quiz", new ArrayList<>());
        try {
            // Save quiz to DB and get generated ID
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

        Question newQuestion = new Question("", Arrays.asList("", "", "", ""), new ArrayList<>(), -1);  // id = -1 for new question
        currentQuiz.getQuestions().add(newQuestion);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
