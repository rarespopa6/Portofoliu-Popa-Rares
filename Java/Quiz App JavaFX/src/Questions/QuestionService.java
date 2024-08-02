package Questions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class QuestionService {
    private ArrayList<Question> questions = new ArrayList<>();

    public QuestionService() {
        this.loadQuestionsFromCSV("questions.csv");
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public void loadQuestionsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 7) {
                    System.out.println("Invalid line in CSV: " + line);
                    continue;
                }

                int id = Integer.parseInt(values[0]);
                String text = values[1];
                String a = values[2];
                String b = values[3];
                String c = values[4];
                String d = values[5];
                String correct = values[6];

                Question question = new Question(id, text, a, b, c, d, correct);
                addQuestion(question);
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }
}
