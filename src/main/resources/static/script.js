let questions = [];
let currentQuestion = 0;
let selectedAnswer = null;
let userAnswers = [];

async function startQuiz() {
    try {
        const response = await fetch("/api/quiz/questions");
        questions = await response.json();

        currentQuestion = 0;
        userAnswers = [];
        selectedAnswer = null;

        document.getElementById("start-screen").style.display = "none";
        document.getElementById("result-screen").style.display = "none";
        document.getElementById("quiz-screen").style.display = "block";

        showQuestion();
    } catch (error) {
        console.error("Error loading questions:", error);
    }
}

function showQuestion() {
    const q = questions[currentQuestion];

    document.getElementById("question-number").textContent =
        "QUESTION " + (currentQuestion + 1) + " OF " + questions.length;
    document.getElementById("question-text").textContent = q.question;

    const progress = ((currentQuestion + 1) / questions.length) * 100;
    document.getElementById("progress-bar").style.width = progress + "%";

    document.getElementById("optionA-text").textContent = q.optionA;
    document.getElementById("optionB-text").textContent = q.optionB;
    document.getElementById("optionC-text").textContent = q.optionC;
    document.getElementById("optionD-text").textContent = q.optionD;

    selectedAnswer = null;
    document.getElementById("next-button").disabled = true;

    const options = document.querySelectorAll(".option-card");
    for (let i = 0; i < options.length; i++) {
        options[i].classList.remove("selected");
    }
}

function selectAnswer(answer) {
    selectedAnswer = answer;

    const options = document.querySelectorAll(".option-card");
    for (let i = 0; i < options.length; i++) {
        options[i].classList.remove("selected");
    }

    document.getElementById("option" + answer).classList.add("selected");
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
    try {
        const response = await fetch("/api/quiz/submit", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userAnswers)
        });

        const result = await response.json();

        document.getElementById("quiz-screen").style.display = "none";
        document.getElementById("result-screen").style.display = "block";

        document.getElementById("score-text").textContent = result.score + "%";
        document.getElementById("correct-text").textContent =
            result.correctAnswers + " out of " + result.totalQuestions + " correct";
    } catch (error) {
        console.error("Error submitting quiz:", error);
    }
}

function restartQuiz() {
    currentQuestion = 0;
    selectedAnswer = null;
    userAnswers = [];

    document.getElementById("result-screen").style.display = "none";
    document.getElementById("start-screen").style.display = "block";
}