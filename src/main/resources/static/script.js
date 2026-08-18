// State Management
let questions = [];
let currentQuestionIndex = 0;
let userAnswers = {}; // { questionId: selectedOptionIndex }

// DOM Elements
const startScreen = document.getElementById('startScreen');
const quizScreen = document.getElementById('quizScreen');
const resultScreen = document.getElementById('resultScreen');

const startBtn = document.getElementById('startBtn');
const prevBtn = document.getElementById('prevBtn');
const nextBtn = document.getElementById('nextBtn');
const submitBtn = document.getElementById('submitBtn');
const restartBtn = document.getElementById('restartBtn');

const currentQuestionNumEl = document.getElementById('currentQuestionNum');
const totalQuestionsNumEl = document.getElementById('totalQuestionsNum');
const totalQuestionsCountEl = document.getElementById('totalQuestionsCount');
const progressBarEl = document.getElementById('progressBar');
const questionTextEl = document.getElementById('questionText');
const optionsContainerEl = document.getElementById('optionsContainer');

const scorePercentageEl = document.getElementById('scorePercentage');
const scoreRingProgressEl = document.getElementById('scoreRingProgress');
const feedbackTitleEl = document.getElementById('feedbackTitle');
const feedbackTextEl = document.getElementById('feedbackText');
const correctAnswersStatEl = document.getElementById('correctAnswersStat');
const totalQuestionsStatEl = document.getElementById('totalQuestionsStat');
const reviewContainerEl = document.getElementById('reviewContainer');

// Default fallback questions in case API is offline during UI testing
const fallbackQuestions = [
    {
        id: 1,
        question: "Which programming language is known as the backbone of Spring Boot?",
        options: ["Python", "Java", "C++", "JavaScript"]
    },
    {
        id: 2,
        question: "What is the default port for an embedded Tomcat server in Spring Boot?",
        options: ["8080", "3000", "5000", "8000"]
    },
    {
        id: 3,
        question: "Which annotation marks a class as a Spring Boot entry point?",
        options: ["@Controller", "@SpringBootApplication", "@Service", "@Component"]
    },
    {
        id: 4,
        question: "Which HTTP method is commonly used to submit quiz answers?",
        options: ["GET", "DELETE", "POST", "HEAD"]
    },
    {
        id: 5,
        question: "What format is most commonly used for data exchange in modern RESTful APIs?",
        options: ["XML", "JSON", "CSV", "YAML"]
    }
];

// Fetch questions from backend
async function loadQuestions() {
    try {
        const response = await fetch('/api/quiz/questions');
        if (response.ok) {
            questions = await response.json();
        } else {
            console.warn('API returned non-OK status, falling back to built-in questions');
            questions = fallbackQuestions;
        }
    } catch (err) {
        console.warn('Using fallback questions:', err);
        questions = fallbackQuestions;
    }

    totalQuestionsCountEl.textContent = questions.length;
    totalQuestionsNumEl.textContent = questions.length;
}

// Start Quiz
function startQuiz() {
    userAnswers = {};
    currentQuestionIndex = 0;
    startScreen.classList.add('hidden');
    resultScreen.classList.add('hidden');
    quizScreen.classList.remove('hidden');
    quizScreen.classList.add('fade-in');
    renderQuestion();
}

// Render Current Question
function renderQuestion() {
    const q = questions[currentQuestionIndex];
    currentQuestionNumEl.textContent = currentQuestionIndex + 1;
    questionTextEl.textContent = q.question;

    // Progress Bar
    const progressPercent = ((currentQuestionIndex + 1) / questions.length) * 100;
    progressBarEl.style.width = `${progressPercent}%`;

    // Render Options
    optionsContainerEl.innerHTML = '';
    const letters = ['A', 'B', 'C', 'D', 'E', 'F'];

    q.options.forEach((opt, idx) => {
        const btn = document.createElement('button');
        btn.className = `option-btn ${userAnswers[q.id] === idx ? 'selected' : ''}`;
        btn.innerHTML = `
            <span class="option-letter">${letters[idx] || idx + 1}</span>
            <span class="option-text">${opt}</span>
        `;
        btn.addEventListener('click', () => selectOption(q.id, idx));
        optionsContainerEl.appendChild(btn);
    });

    // Navigation buttons state
    prevBtn.disabled = currentQuestionIndex === 0;
    
    if (currentQuestionIndex === questions.length - 1) {
        nextBtn.classList.add('hidden');
        submitBtn.classList.remove('hidden');
    } else {
        nextBtn.classList.remove('hidden');
        submitBtn.classList.add('hidden');
    }
}

// Select an option
function selectOption(questionId, optionIndex) {
    userAnswers[questionId] = optionIndex;
    renderQuestion();
}

// Navigate
function prevQuestion() {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        renderQuestion();
    }
}

function nextQuestion() {
    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        renderQuestion();
    }
}

// Submit Quiz
async function submitQuiz() {
    submitBtn.disabled = true;
    submitBtn.textContent = "Grading...";

    let result;
    try {
        const response = await fetch('/api/quiz/submit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ answers: userAnswers })
        });

        if (response.ok) {
            result = await response.json();
        } else {
            result = localEvaluate();
        }
    } catch (e) {
        result = localEvaluate();
    }

    displayResults(result);
    submitBtn.disabled = false;
    submitBtn.innerHTML = `<span>Submit Quiz</span><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 6L9 17l-5-5"/></svg>`;
}

// Fallback local evaluation
function localEvaluate() {
    const correctMap = { 1: 1, 2: 0, 3: 1, 4: 2, 5: 1 };
    let correct = 0;
    questions.forEach(q => {
        if (userAnswers[q.id] === correctMap[q.id]) {
            correct++;
        }
    });
    const percentage = Math.round((correct / questions.length) * 100);
    return {
        totalQuestions: questions.length,
        correctAnswers: correct,
        scorePercentage: percentage,
        feedback: percentage >= 80 ? "Great job! You know your concepts! 🚀" : "Keep learning and practicing! 💪",
        correctOptions: correctMap
    };
}

// Display Results
function displayResults(result) {
    quizScreen.classList.add('hidden');
    resultScreen.classList.remove('hidden');
    resultScreen.classList.add('fade-in');

    scorePercentageEl.textContent = `${result.scorePercentage}%`;
    correctAnswersStatEl.textContent = result.correctAnswers;
    totalQuestionsStatEl.textContent = result.totalQuestions;
    feedbackTextEl.textContent = result.feedback;

    if (result.scorePercentage >= 80) {
        feedbackTitleEl.textContent = "🎉 Excellent Performance!";
    } else if (result.scorePercentage >= 50) {
        feedbackTitleEl.textContent = "👍 Good Job!";
    } else {
        feedbackTitleEl.textContent = "📚 Keep Practicing!";
    }

    // Animate circular progress ring (Circumference = 2 * PI * 50 = ~314)
    const offset = 314 - (314 * result.scorePercentage) / 100;
    setTimeout(() => {
        scoreRingProgressEl.style.strokeDashoffset = offset;
    }, 100);

    // Build Review List
    reviewContainerEl.innerHTML = '';
    questions.forEach((q, index) => {
        const userChoiceIdx = userAnswers[q.id];
        const correctChoiceIdx = result.correctOptions ? result.correctOptions[q.id] : undefined;
        const isCorrect = userChoiceIdx !== undefined && userChoiceIdx === correctChoiceIdx;

        const userChoiceText = userChoiceIdx !== undefined ? q.options[userChoiceIdx] : "Unanswered";
        const correctChoiceText = correctChoiceIdx !== undefined ? q.options[correctChoiceIdx] : "N/A";

        const item = document.createElement('div');
        item.className = 'review-item';
        item.innerHTML = `
            <div class="review-question">${index + 1}. ${q.question}</div>
            <div class="review-answers">
                <div class="ans-your ${isCorrect ? 'correct' : 'incorrect'}">
                    <strong>Your Answer:</strong> ${userChoiceText} ${isCorrect ? '✓' : '✗'}
                </div>
                ${!isCorrect ? `<div class="ans-correct"><strong>Correct Answer:</strong> ${correctChoiceText}</div>` : ''}
            </div>
        `;
        reviewContainerEl.appendChild(item);
    });
}

// Event Listeners
startBtn.addEventListener('click', startQuiz);
prevBtn.addEventListener('click', prevQuestion);
nextBtn.addEventListener('click', nextQuestion);
submitBtn.addEventListener('click', submitQuiz);
restartBtn.addEventListener('click', startQuiz);

// Initialize on page load
document.addEventListener('DOMContentLoaded', loadQuestions);
