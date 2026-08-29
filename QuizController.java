public class QuizController {

    String[] questions = {
        "Which keyword is used to create a class in Java?",
        "Which method is the starting point of a Java program?",
        "Which data type is used to store whole numbers in Java?",
        "Which keyword is used to create an object in Java?",
        "Which symbol is used to end a statement in Java?"
    };

    String[][] options = {
        {"function", "class", "create", "object"},
        {"start()", "run()", "main()", "begin()"},
        {"String", "int", "boolean", "double"},
        {"new", "object", "create", "class"},
        {".", ":", ";", ","}
    };

    String[] correctAnswers = {
        "B",
        "C",
        "B",
        "A",
        "C"
    };


    public String getQuestions() {

        String result = "[";

        for (int i = 0; i < questions.length; i++) {

            result += "{";
            result += "\"id\":" + (i + 1) + ",";
            result += "\"question\":\"" + questions[i] + "\",";
            result += "\"optionA\":\"" + options[i][0] + "\",";
            result += "\"optionB\":\"" + options[i][1] + "\",";
            result += "\"optionC\":\"" + options[i][2] + "\",";
            result += "\"optionD\":\"" + options[i][3] + "\"";
            result += "}";

            if (i < questions.length - 1) {
                result += ",";
            }
        }

        result += "]";

        return result;
    }


    public String checkAnswers(String answers) {

        int score = 0;

        for (int i = 0; i < correctAnswers.length; i++) {

            String answer = "\"questionId\":" + (i + 1)
                    + ",\"selectedAnswer\":\""
                    + correctAnswers[i] + "\"";

            if (answers.contains(answer)) {
                score++;
            }
        }

        int percentage = score * 100 / questions.length;

        return "{"
                + "\"totalQuestions\":" + questions.length + ","
                + "\"correctAnswers\":" + score + ","
                + "\"score\":" + percentage
                + "}";
    }
}