#  Release Plan – DeepSeekers Teaching App

This release plan outlines the strategic guide for the development and launch of the *DeepSeekers* education application. While some key project parameters are pre-defined (e.g., timeline is fixed to the teaching semester), this plan documents scope, resources, and known dependencies to provide clarity for the project team and stakeholders.

---

##  Scope

The following features are planned for release during the semester:

### User Authentication
- Student and teacher sign-up/login
- Role-based redirection (student dashboard / teacher dashboard)

### Subject Management
- Students select up to 4 subjects during sign-up
- Teachers view the subject they are assigned to

### Content Release & Access
- Teachers can release assignments, homeworks, and study materials with AI assisted generation
- Students can view and interact with released content

### Submissions & Overview
- Students submit assignments and homeworks
- Teachers access a student overview dashboard and grade the students' sumbittion
- 
##  Out of scope

### Test page
- Teacher release test and grading
- Student access test content and submission

### Other out of scope feature
- Auto-grading submissions
- Plagiarism checker for submissions
- Flashcard generation for students
- Cheating detection for submissions
---

## Timeline

| **Phase**   | **Duration** | **Tasks** |
|-------------|--------------|-----------|
| Sprint 1    | Week 5–7     | Determine project management tool, write user stories, create low/medium fidelity UI design & prototype, configure project management tool |
| Sprint 2    | Week 7-9       | Create javaFX UI codes for “must-have” pages: sign in/sign up, profile page, user viewing assignment, homework page, subject homepage, dashboard, class info & study material.  |
| Sprint 3    | Week 9-11       | Code “should-have” pages: teacher releases (assignments, homework, study material), grading, student submissions & test participation. Create SQLite database storing the created user, files, homework and assignment. Record a walk through vedio for the demo |
| Sprint 4    | Week 11-13      | Code “should-have” functuions: teacher using AI assist in homework, assignment and lesson plan. Refactoring and cleaning the existing code |

---

##  Resources

### Team Members:
- **Backend:** Angela Suresh Thomas, Glen Joshy Pallan, Kaiyu Shen, Navaneeth, Max Padovan
- **Frontend/Controllers:** Angela Suresh Thomas, Glen Joshy Pallan, Kaiyu Shen, Navaneeth, Max Padovan
- **Testing:**  Angela Suresh Thomas, Glen Joshy Pallan, Kaiyu Shen, Navaneeth, Max Padovan
- **Integration:** Navaneeth

### Tools:
- Java 21, JavaFX, SQLite
- IntelliJ IDEA, GitHub, JUnit
- Ollama4j

### Infrastructure:
- Local development environments
- GitHub for source control and CI/CD

---

##  Agile Adaptability

This plan serves as a living document. As project requirements or team availability changes, this plan may evolve. All updates will be version-controlled and documented to maintain a clear record of decisions and their rationale.
