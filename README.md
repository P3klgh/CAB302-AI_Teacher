# CAB302-AI_Teacher

## What is this?

This is a JavaFX desktop app we built for CAB302.  
It's made to help teachers run classrooms better — with some AI stuff like quiz generation and a chatbot to help students study.

We used Agile (2-week sprints), worked in a team, and wrote most of it in Java using MVC and good OO practices.

---

## Features (main stuff that works)

- 🧠 **AI Quiz Maker**  
  You can either make quizzes yourself or let the app generate one for you using AI.

- 🤖 **AI Chatbot**  
  If a student gets stuck, they can ask questions and get explanations or study resources.

---

## Tech Stack

- Java 17+
- JavaFX for GUI
- SQLite for storing stuff
- Maven for building
- JUnit for testing
- Git + GitHub for version control

---

## How to Run It

1. Install Java 17 or higher
2. Open the project in IntelliJ (or another Java IDE)
3. Make sure JavaFX is set up properly
4. Go to this file: src/main/java/com/cab302ai_teacher/Main.java


5. Run it (should open the login screen)

---

## How to Test It

- We wrote some basic unit tests to check login and registration logic.
- You can find them in `src/test/`.
- You can run them individually. (e.g. DatabaseManagerTest, UserDAOTest and ValidatorTest)
- Some tests are meant to **fail on purpose**, like when trying to register a duplicate email — because our DB doesn't allow two users with the same email (`UNIQUE constraint`).

---

## Notes

- Passwords are securely hashed using SHA-256 when stored.
- Make sure to delete `ai_teacher.db` if you want to start fresh — it'll regenerate with the correct schema.
- We also used **Trello** for sprint planning and GitHub for version control throughout the project.
