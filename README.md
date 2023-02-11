# Gradle Build Bible

build script = build.gradle or build.gradle.kts

The highest level Gradle concept is the project. Builds scripts configures project. Project is a Java object.

## Build lifecycle

- Initialization - finds out what projects take part in our build
- Configuration - task preparation, creates model of our projects
- Execution - executes tasks using command line settings
