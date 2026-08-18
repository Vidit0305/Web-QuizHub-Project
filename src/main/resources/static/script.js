let questions = [];

let currentQuestionIndex = 0;

let selectedAnswer = null;

let userAnswers = [];

let currentCategory = "";


/* ==============================
   START QUIZ
============================== */

async function startQuiz(category) {

    currentCategory = category;

    try {

        const response = await fetch(
            `/api/questions?category=${category}`
        );

        questions = await response.json();

        if (questions.length === 0) {

            alert("No questions found.");

            return;
        }

        currentQuestionIndex = 0;

        userAnswers = [];

        showScreen("quizScreen");

        document.getElementById(
            "quizCategory"
        ).textContent = category;

        document.getElementById(
            "totalQuestions"
        ).textContent = questions.length;

        showQuestion();

    } catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the Java backend."
        );
    }
}


/* ==============================
   SHOW QUESTION
============================== */

function showQuestion() {

    const question =
        questions[currentQuestionIndex];

    selectedAnswer = null;

    document.getElementById(
        "currentQuestion"
    ).textContent =
        currentQuestionIndex + 1;


    document.getElementById(
        "questionText"
    ).textContent =
        question.question;


    const progress =
        ((currentQuestionIndex + 1)
            / questions.length) * 100;


    document.getElementById(
        "progressBar"
    ).style.width =
        `${progress}%`;


    const optionsContainer =
        document.getElementById(
            "optionsContainer"
        );


    optionsContainer.innerHTML = "";


    const options = [

        {
            letter: "A",
            text: question.optionA
        },

        {
            letter: "B",
            text: question.optionB
        },

        {
            letter: "C",
            text: question.optionC
        },

        {
            letter: "D",
            text: question.optionD
        }

    ];


    options.forEach(option => {

        const button =
            document.createElement("button");

        button.className = "option";


        button.innerHTML = `
            <span class="option-letter">
                ${option.letter}
            </span>

            ${option.text}
        `;


        button.onclick = function () {

            selectAnswer(
                option.letter,
                button
            );

        };


        optionsContainer.appendChild(button);

    });


    const nextButton =
        document.getElementById(
            "nextButton"
        );


    if (
        currentQuestionIndex ===
        questions.length - 1
    ) {

        nextButton.textContent =
            "Submit Quiz";

    } else {

        nextButton.textContent =
            "Next Question →";
    }
}


/* ==============================
   SELECT ANSWER
============================== */

function selectAnswer(
    answer,
    button
) {

    selectedAnswer = answer;


    const allOptions =
        document.querySelectorAll(
            ".option"
        );


    allOptions.forEach(option => {

        option.classList.remove(
            "selected"
        );

    });


    button.classList.add("selected");
}


/* ==============================
   NEXT QUESTION
============================== */

function nextQuestion() {

    if (selectedAnswer === null) {

        alert(
            "Please select an answer first."
        );

        return;
    }


    const question =
        questions[currentQuestionIndex];


    userAnswers.push({

        questionId: question.id,

        selectedAnswer: selectedAnswer

    });


    if (
        currentQuestionIndex <
        questions.length - 1
    ) {

        currentQuestionIndex++;

        showQuestion();

    } else {

        submitQuiz();

    }
}


/* ==============================
   SUBMIT QUIZ
============================== */

async function submitQuiz() {

    try {

        const response = await fetch(
            "/api/submit",
            {

                method: "POST",

                headers: {
                    "Content-Type":
                        "application/json"
                },

                body:
                    JSON.stringify(userAnswers)

            }
        );


        const result =
            await response.json();


        showResult(result);


    } catch (error) {

        console.error(error);

        alert(
            "Unable to submit the quiz."
        );
    }
}


/* ==============================
   SHOW RESULT
============================== */

function showResult(result) {

    document.getElementById(
        "scoreText"
    ).textContent =
        `${result.correctAnswers}/${result.totalQuestions}`;


    document.getElementById(
        "correctAnswers"
    ).textContent =
        result.correctAnswers;


    document.getElementById(
        "wrongAnswers"
    ).textContent =
        result.wrongAnswers;


    document.getElementById(
        "percentage"
    ).textContent =
        `${Math.round(result.percentage)}%`;


    let message;


    if (result.percentage >= 80) {

        message =
            "Excellent performance!";

    } else if (result.percentage >= 60) {

        message =
            "Good job! Keep improving.";

    } else if (result.percentage >= 40) {

        message =
            "Not bad. A little more practice will help.";

    } else {

        message =
            "Keep practicing and try again!";

    }


    document.getElementById(
        "performanceText"
    ).textContent =
        message;


    showScreen("resultScreen");
}


/* ==============================
   RESTART QUIZ
============================== */

function restartQuiz() {

    startQuiz(currentCategory);

}


/* ==============================
   GO HOME
============================== */

function goHome() {

    showScreen("homeScreen");

}


/* ==============================
   SCREEN MANAGEMENT
============================== */

function showScreen(screenId) {

    const screens =
        document.querySelectorAll(
            ".screen"
        );


    screens.forEach(screen => {

        screen.classList.remove(
            "active"
        );

    });


    document.getElementById(
        screenId
    ).classList.add("active");

}