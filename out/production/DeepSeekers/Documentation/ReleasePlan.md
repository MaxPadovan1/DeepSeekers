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
- Teachers can release assignments, tests, and study material
- Students can view and interact with released content

### Submissions & Overview
- Students submit assignments and take tests
- Teachers access a student overview dashboard

---

## Timeline

| **Phase**   | **Duration** | **Tasks** |
|-------------|--------------|-----------|
| Sprint 1    | Week 5–7     | Determine project management tool, write user stories, create low/medium fidelity UI design & prototype, configure project management tool |
| Sprint 2    | Week 8       | Code “must-have” pages: sign in/sign up, profile page, user viewing assignment, homework/test page, subject homepage, dashboard, class info & study material |
| Sprint 3    | Week 9       | Code “should-have” pages: teacher releases (tests, assignments, homework, study material), grading, student submissions & test participation |
| Sprint 4    | Week 10      | Code “can-have” pages: user views class info, teacher checks for cheating |

---

##  Resources

### Team Members:
- **Backend:** Navaneeth, Max Padovan
- **Frontend/Controllers:** Angela Suresh Thomas, Glen Joshy Pallan, Kaiyu Shen
- **Testing & Integration:** Navaneeth

### Tools:
- Java 21, JavaFX, SQLite
- IntelliJ IDEA, GitHub, JUnit

### Infrastructure:
- Local development environments
- GitHub for source control and CI/CD

---

##  Agile Adaptability

This plan serves as a living document. As project requirements or team availability changes, this plan may evolve. All updates will be version-controlled and documented to maintain a clear record of decisions and their rationale.