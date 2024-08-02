import Questions.Question;
import Questions.QuestionService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Collections;
import java.util.List;

public class QuizApp extends Application {

    private QuestionService questionService = new QuestionService();
    private int score = 0;
    private int currentQuestionIndex = 0;
    private List<Question> currentQuestions;
    private Label questionLabel = new Label();
    private ToggleGroup optionsGroup = new ToggleGroup();
    private Label scoreLabel = new Label();
    private Button startButton = new Button("Start Game");
    private Button playAgainButton = new Button("Play Again");
    private RadioButton optionA = new RadioButton();
    private RadioButton optionB = new RadioButton();
    private RadioButton optionC = new RadioButton();
    private RadioButton optionD = new RadioButton();
    private Timeline questionTimer;
    private Timeline answerDelayTimer;
    private Stage stage;
    private boolean isAnswering = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // GUI
        VBox root = new VBox(20);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        root.getChildren().addAll(startButton);

        startButton.setStyle("-fx-font-size: 16px; -fx-padding: 10;");

        startButton.setOnAction(e -> startQuiz());
        playAgainButton.setOnAction(e -> restartQuiz());
        playAgainButton.setVisible(false);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Quiz Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startQuiz() {
        currentQuestions = getRandomQuestions(10);
        score = 0;
        currentQuestionIndex = 0;
        scoreLabel.setText("");

        // GUI
        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");
        HBox optionsBox = new HBox(10);
        optionsBox.setStyle("-fx-alignment: center;");
        optionsBox.getChildren().addAll(optionA, optionB, optionC, optionD);

        root.getChildren().addAll(questionLabel, optionsBox, scoreLabel, playAgainButton);
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        showQuestion(currentQuestions.get(currentQuestionIndex));
    }

    private void showQuestion(Question question) {
        questionLabel.setText(question.getText());
        questionLabel.setStyle("-fx-font-size: 18px;");

        optionA.setText("a) " + question.getA());
        optionB.setText("b) " + question.getB());
        optionC.setText("c) " + question.getC());
        optionD.setText("d) " + question.getD());

        optionA.setToggleGroup(optionsGroup);
        optionB.setToggleGroup(optionsGroup);
        optionC.setToggleGroup(optionsGroup);
        optionD.setToggleGroup(optionsGroup);

        optionsGroup.selectToggle(null);

        if (questionTimer != null) {
            questionTimer.stop();
        }
        questionTimer = new Timeline(new KeyFrame(Duration.seconds(10), e -> handleTimeout()));
        questionTimer.setCycleCount(1);
        questionTimer.play();

        questionLabel.setVisible(true);
        optionA.setVisible(true);
        optionB.setVisible(true);
        optionC.setVisible(true);
        optionD.setVisible(true);

        if (optionsGroup.selectedToggleProperty().get() == null) {
            optionsGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
                if (newToggle != null && !isAnswering) {
                    RadioButton selectedOption = (RadioButton) newToggle;
                    isAnswering = true;
                    checkAnswer(selectedOption.getText().charAt(0));
                }
            });
        }
    }

    private void handleTimeout() {
        scoreLabel.setText("Time's up! No answer provided. Score: " + score);
        hideQuestion();
        startAnswerDelayTimer();
    }

    private void checkAnswer(char selectedOption) {
        if (questionTimer != null) {
            questionTimer.stop();
        }
        Question currentQuestion = currentQuestions.get(currentQuestionIndex);
        if (selectedOption == currentQuestion.getCorrect().charAt(0)) {
            score++;
            scoreLabel.setText("Correct! Score: " + score);
        } else {
            scoreLabel.setText("Incorrect! The correct answer was: " + currentQuestion.getCorrect() + ". Score: " + score);
        }

        hideQuestion();
        startAnswerDelayTimer();
    }

    private void hideQuestion() {
        questionLabel.setVisible(false);
        optionA.setVisible(false);
        optionB.setVisible(false);
        optionC.setVisible(false);
        optionD.setVisible(false);
    }

    private void startAnswerDelayTimer() {
        if (answerDelayTimer != null) {
            answerDelayTimer.stop();
        }
        answerDelayTimer = new Timeline(new KeyFrame(Duration.seconds(2), e -> goToNextQuestion()));
        answerDelayTimer.setCycleCount(1);
        answerDelayTimer.play();
    }

    private void goToNextQuestion() {
        scoreLabel.setText("");
        currentQuestionIndex++;
        isAnswering = false;
        if (currentQuestionIndex < currentQuestions.size()) {
            showQuestion(currentQuestions.get(currentQuestionIndex));
        } else {
            endQuiz();
        }
    }

    private void endQuiz() {
        scoreLabel.setText("Final score: " + score);
        playAgainButton.setVisible(true);
    }

    private void restartQuiz() {
        playAgainButton.setVisible(false);
        startQuiz();
    }

    private List<Question> getRandomQuestions(int numQuestions) {
        List<Question> allQuestions = questionService.getQuestions();
        Collections.shuffle(allQuestions);
        return allQuestions.subList(0, Math.min(numQuestions, allQuestions.size()));
    }
}
