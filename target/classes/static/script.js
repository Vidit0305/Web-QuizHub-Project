let questions = [];

let currentQuestion = 0;

let selectedAnswer = null;

let userAnswers = [];


async function startQuiz() {

    const response = await fetch("/api/quiz/questions");

    questions = await response.json();

    document.getElementById("start-screen").style.display = "none";

    document.getElementById("quiz-screen").style.display = "block";

    showQuestion();
}


function showQuestion() {

    const question = questions[currentQuestion];

    document.getElementById("question-number").textContent =
        "Question " + (currentQuestion + 1) + " of " + questions.length;

    document.getElementById("question-text").textContent =
        question.question;

    document.getElementById("optionA").textContent =
        "A. " + question.optionA;

    document.getElementById("optionB").textContent =
        "B. " + question.optionB;

    document.getElementById("optionC").textContent =
        "C. " + question.optionC;

    document.getElementById("optionD").textContent =
        "D. " + question.optionD;

    selectedAnswer = null;

    document.getElementById("next-button").disabled = true;

    document.querySelectorAll(".option").forEach(function (button) {

        button.classList.remove("selected");

    });
}


function selectAnswer(answer) {

    selectedAnswer = answer;

    document.querySelectorAll(".option").forEach(function (button) {

        button.classList.remove("selected");

    });

    document.getElementById("option" + answer)
        .classList.add("selected");

    document.getElementById("next-button").disabled = false;
}


function nextQuestion() {

    userAnswers.push({

        questionId: questions[currentQuestion].id,

        selectedAnswer: selectedAnswer

    });


    currentQuestion++;


    if (currentQuestion < questions.length) {

        showQuestion();

    } else {

        submitQuiz();

    }
}


async function submitQuiz() {

    const response = await fetch("/api/quiz/submit", {

        method: "POST",

        headers: {
            "Content-Type": "application/json"
        },

        body: JSON.stringify(userAnswers)

    });


    const result = await response.json();

    document.getElementById("quiz-screen").style.display = "none";

    document.getElementById("result-screen").style.display = "block";

    document.getElementById("score-text").textContent =
        "Score: " + result.score + " / 100";

    document.getElementById("correct-text").textContent =
        "Correct Answers: " +
        result.correctAnswers +
        " / " +
        result.totalQuestions;
}


function restartQuiz() {

    currentQuestion = 0;

    selectedAnswer = null;

    userAnswers = [];

    document.getElementById("result-screen").style.display = "none";

    document.getElementById("start-screen").style.display = "block";
}