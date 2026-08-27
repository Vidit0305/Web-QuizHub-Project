// State variables
let questions = [];
let currentIndex = 0;
let selectedAnswer = null;
let userAnswers = [];

// Start Quiz: fetch questions from backend and initialize state
async function startQuiz() {
    hideError();
    try {
        const response = await fetch("/api/questions");
        if (!response.ok) {
            throw new Error("Failed to load questions: " + response.status);
        }
        questions = await response.json();

        currentIndex = 0;
        selectedAnswer = null;
        userAnswers = [];

        document.getElementById("start-screen").style.display = "none";
        document.getElementById("result-screen").style.display = "none";
        document.getElementById("quiz-screen").style.display = "block";

        renderQuestion();
    } catch (error) {
        console.error("Error starting quiz:", error);
        showError("Unable to connect to the quiz server. Please ensure the server is running.");
    }
}

// Render the current question and options
function renderQuestion() {
    const q = questions[currentIndex];

    document.getElementById("question-number").textContent =
        "Question " + (currentIndex + 1) + " of " + questions.length;

    const progress = ((currentIndex + 1) / questions.length) * 100;
    document.getElementById("progress-bar").style.width = progress + "%";

    document.getElementById("question-text").textContent = q.question;
    document.getElementById("optionA-text").textContent = q.optionA;
    document.getElementById("optionB-text").textContent = q.optionB;
    document.getElementById("optionC-text").textContent = q.optionC;
    document.getElementById("optionD-text").textContent = q.optionD;

    // Reset selection and button state
    selectedAnswer = null;
    document.getElementById("next-button").disabled = true;

    const options = document.querySelectorAll(".option-card");
    options.forEach(option => option.classList.remove("selected"));
}

// Handle option selection
function selectAnswer(option) {
    selectedAnswer = option;

    const options = document.querySelectorAll(".option-card");
    options.forEach(card => card.classList.remove("selected"));

    document.getElementById("option" + option).classList.add("selected");
    document.getElementById("next-button").disabled = false;
}

// Move to next question or submit if final question reached
function nextQuestion() {
    if (!selectedAnswer) return;

    userAnswers.push({
        questionId: questions[currentIndex].id,
        selectedAnswer: selectedAnswer
    });

    currentIndex++;

    if (currentIndex < questions.length) {
        renderQuestion();
    } else {
        submitQuiz();
    }
}

// Submit answers to Java backend and display results
async function submitQuiz() {
    hideError();
    try {
        const response = await fetch("/api/submit", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(userAnswers)
        });

        if (!response.ok) {
            throw new Error("Failed to submit quiz: " + response.status);
        }

        const result = await response.json();

        document.getElementById("quiz-screen").style.display = "none";
        document.getElementById("result-screen").style.display = "block";

        document.getElementById("score-fraction").textContent =
            result.correctAnswers + " / " + result.totalQuestions;
        document.getElementById("score-text").textContent = result.score + "%";
        document.getElementById("score-message").textContent = getFeedbackMessage(result.score);
    } catch (error) {
        console.error("Error submitting quiz:", error);
        showError("Unable to submit quiz. Please check your connection to the server.");
    }
}

// Generate score feedback message
function getFeedbackMessage(score) {
    if (score === 100) {
        return "Perfect score! Outstanding Java knowledge.";
    } else if (score >= 80) {
        return "Great job! You have a solid grasp of Java basics.";
    } else if (score >= 60) {
        return "Good effort! Keep practicing to sharpen your skills.";
    } else {
        return "Keep learning! Review Java basics and try again.";
    }
}

// Restart quiz from result screen
function restartQuiz() {
    currentIndex = 0;
    selectedAnswer = null;
    userAnswers = [];
    hideError();

    document.getElementById("result-screen").style.display = "none";
    document.getElementById("start-screen").style.display = "block";
}

// Error UI helpers
function showError(message) {
    const banner = document.getElementById("error-banner");
    if (banner) {
        banner.textContent = message;
        banner.style.display = "block";
    }
}

function hideError() {
    const banner = document.getElementById("error-banner");
    if (banner) {
        banner.style.display = "none";
    }
}
