public class QuizController {

    // Simple Question structure inside this file
    static class Question {
        int id;
        String text;
        String optionA;
        String optionB;
        String optionC;
        String optionD;
        String correctOption;

        Question(int id, String text, String a, String b, String c, String d, String correct) {
            this.id = id;
            this.text = text;
            this.optionA = a;
            this.optionB = b;
            this.optionC = c;
            this.optionD = d;
            this.correctOption = correct;
        }
    }

    private Question[] questions;

    public QuizController() {
        questions = new Question[] {
            new Question(1, "Which keyword is used to create a class in Java?", "function", "class", "create", "object", "B"),
            new Question(2, "Which method is the starting point of a Java program?", "start()", "run()", "main()", "begin()", "C"),
            new Question(3, "Which data type is used to store whole numbers in Java?", "String", "int", "boolean", "double", "B"),
            new Question(4, "Which keyword is used to create an object in Java?", "new", "object", "create", "class", "A"),
            new Question(5, "Which symbol is used to end a statement in Java?", ".", ":", ";", ",", "C")
        };
    }

    // Returns JSON array of questions without exposing the correct answer
    public String getQuestionsJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < questions.length; i++) {
            Question q = questions[i];
            sb.append("{");
            sb.append("\"id\":").append(q.id).append(",");
            sb.append("\"question\":\"").append(escapeJson(q.text)).append("\",");
            sb.append("\"optionA\":\"").append(escapeJson(q.optionA)).append("\",");
            sb.append("\"optionB\":\"").append(escapeJson(q.optionB)).append("\",");
            sb.append("\"optionC\":\"").append(escapeJson(q.optionC)).append("\",");
            sb.append("\"optionD\":\"").append(escapeJson(q.optionD)).append("\"");
            sb.append("}");
            if (i < questions.length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    // Receives user answers JSON and calculates score
    public String calculateResultJson(String body) {
        int correct = 0;
        int total = questions.length;

        // Clean spaces to ensure straightforward matching
        String cleanBody = body.replaceAll("\\s+", "");

        for (Question q : questions) {
            String tag1 = "\"questionId\":" + q.id + ",\"selectedAnswer\":\"" + q.correctOption + "\"";
            String tag2 = "\"selectedAnswer\":\"" + q.correctOption + "\",\"questionId\":" + q.id;
            String tag3 = "\"questionId\":" + q.id + ",\"selectedAnswer\":\"" + q.correctOption.toLowerCase() + "\"";
            String tag4 = "\"selectedAnswer\":\"" + q.correctOption.toLowerCase() + "\",\"questionId\":" + q.id;

            if (cleanBody.contains(tag1) || cleanBody.contains(tag2) || cleanBody.contains(tag3) || cleanBody.contains(tag4)) {
                correct++;
            }
        }

        int score = (correct * 100) / total;
        return "{\"totalQuestions\":" + total + ",\"correctAnswers\":" + correct + ",\"score\":" + score + "}";
    }

    private String escapeJson(String s) {
        return s.replace("\"", "\\\"");
    }
}
