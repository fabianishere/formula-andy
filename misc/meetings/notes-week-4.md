# Week 4
- Chairwoman: Laetitia Molkenboer
- Secretary: Nikki Bouman
- Absent: None

## 5 December 2016, 13:45-17:45
### Opening
Done

### Approval agenda
Add one item: demo

### Progress past sprint
#### What has been done?
- Fabian
  Setup repository, TravisCI, Gradle, Checkstyle, JaCoCo and editorconfig 
  configuration. Create database with connection from code. Refactor and test
  code Christian.
- Christian
  Create domain model (game logic). Create game specification with Laetitia
- Nils
  Create and implement design for the loading screen. Create design of the 
  start screen.
- Laetitia
  Create login screen. Create game specification.
- Nikki
  Create and implement design for the start screen. Design the loading screen.

#### Why has Fabian a much larger diagram than the others?
He has done more work than the rest, but he also converted the binary .docx files
to Markdown format, which results in more contributions (since only text 
contributions are counted).

#### What could go better?
- Improve the planning (Laetitia)
- Make task per person more clear (Laetitia)
	* Every person has a issues assigned to them on Github (Fabian)

### Demo
- [x] Database
- [x] Design loading-screen
- [x] Start-screen
- [x] Design game-screen
- [x] Code

### Evaluating technologies
#### Gradle
Gradle allows us to build and test the project' code independently of the user's
configuration, which also allows us to test in the cloud using Travis CI.

#### Travis CI
Travis CI provides free continuous integration services and allows us to test
the code every time a pull request is sent to the master repository.

#### Akka
Akka allows efficient and distributed communication between "actors" via 
message-passing. This is ideal for an event-based architecture.

#### Hibernate/JPA
The Java Persistent API allows the persistence of game objects by saving them to
a database. As a result of using Hibernate as JPA implementation, we can persist
the game state on every database software system supported by Hibernate/JPA.

#### Slf4j
Slf4j allows us to log messages as library and let the user decide what they
want with the messages by choosing their slf4j adapter.

#### JavaFX
JavaFX is the framework we use for creating the user interface. We use FXML files
which are somewhat related to HTML in combination with CSS to style the application.

#### Github (Scrum)
We use Github as scrum board. Every scrum task can be found in on the issue page.
If you want to see the sprints, go to Issues and then Milestones. The scrumboard
can be found on the Projects page.

#### Checkstyle
We should be using the Checkstyle plugin for IntelliJ IDEA to automatically
detect incorrect code style.

### Planning next sprint
- Fabian: Start on networking code and wrapping up game model. Finish persistence.
- Christian: Create the positional algorithm.
- Laetitia: Fill in the database. Work on GUI.
- Nils: Create a team-creating AI
- Nikki: Work on GUI (game screen, login screen, result screen)

### Show test coverage/checkstyle reports
Done

### Issues
- Communication (Fabian)
- Planning (Laetitia)

