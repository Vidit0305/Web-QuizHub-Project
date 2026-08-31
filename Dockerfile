FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY *.java .
COPY index.html .
COPY style.css .
COPY script.js .

RUN javac QuizHub.java QuizController.java

EXPOSE 8080

CMD ["java", "QuizHub"]
