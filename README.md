# CAB302-AI_Teacher

## What is this?

This is a JavaFX desktop application developed for the CAB302 course.  
It assists teachers in managing classrooms and supports AI features such as quiz generation and a chatbot for students.

We followed Agile methodology (two-week sprints), worked collaboratively in a team, and applied object-oriented principles using Java and MVC architecture.

---

## Features

- **Quiz Maker**  
  Create quizzes manually or generate them.

- **AI Chatbot**  
  Students can ask questions and receive study support or explanations.

- **User Management**  
  Teachers and students can log in, edit personal details, and navigate with role-based functionality.

- **Quiz Management**  
  Teachers can create, edit, and delete quizzes, while students can take quizzes and receive scores.

---

## Tech Stack

- Java 17+
- JavaFX (UI)
- SQLite (Database)
- Maven (Build tool)
- JUnit (Testing)
- GitHub Actions (CI/CD)

---

## How to Run It

1. Install Java 17 or newer
2. Open the project in an IDE (e.g., IntelliJ)
3. Set up JavaFX properly
4. Run `src/main/java/com/cab302ai_teacher/Main.java`
5. The application will start with the login screen

---

## How to Test It

- Navigate to `src/test/` to find unit tests for:
  - DatabaseManager
  - UserDAO
  - Validator
- Run tests using your IDE or Maven
- Some test cases are meant to fail (e.g., duplicate email registration)

---

## Security

- Passwords are hashed using SHA-256 before storage
- The app enforces email format and strong password requirements

---

## Continuous Integration

GitHub Actions runs automated workflows on every push and pull request:

- Maven build
- Unit tests
- Javadoc generation
- Test report publication


---

## Team and Responsibilities

| Name                      | Role                       | Contributions                                                                |
| ------------------------- | -------------------------- | ---------------------------------------------------------------------------- |
| Zach (N12038431)          | Designer / Backend Support | Low/Medium fidelity designs, implemented manual quiz, meeting minutes        |
| Kynan Stoakes (N11477504) | Frontend Developer         | UI styling, user edit details, troubleshooting, stage navigation             |
| Kenneth Lee (N11532386)   | Fullstack Developer        | REST API implementation, password hashing, DB management, test case creation |
| Sanghun Han (N11680628)   | Refactorer / CI Integrator | Refactoring using Singleton/Factory patterns, GitHub Actions, Trello setup   |

---

## Notes

- The SQLite database (`ai_teacher.db`) is regenerated if deleted
- Project includes modular Javadoc for all classes and controllers
- Javadoc is auto-deployed to GitHub Pages via CI/CD
