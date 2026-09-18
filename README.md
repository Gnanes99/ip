# Dennis

Dennis is a desktop chatbot that tracks todos, deadlines, and events from a
JavaFX chat window, with a bit of cat personality on the side. It's a Java 25
project built for an individual software engineering assignment.

For the full User Guide, see the [product website](https://gnanes99.github.io/ip/).

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/dennis/gui/Launcher.java` file, right-click it, and choose `Run Launcher.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, the Dennis chat window should open.

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

## Building from source

Prerequisites: JDK 25.

```
./gradlew run         # launch the GUI
./gradlew check       # compile, run the 233 JUnit tests, run Checkstyle
./gradlew shadowJar   # build build/libs/dennis.jar
```

The console version is still there, if you'd rather skip the GUI:

```
java -cp build/classes/java/main dennis.Dennis
```
