# Gradle Build Bible

build script = build.gradle or build.gradle.kts


- _build.gradle_ = Gradle build script definition
- _settings.gradle_ = project name and other project settings
- _build_ directory which containts generated filed and artifacts

The highest level Gradle concept is the project. Builds scripts configures project. Project is a Java object.

## Build lifecycle

- Initialization - finds out what projects take part in our build
- Configuration - task preparation, creates model of our projects
- Execution - executes tasks using command line settings

## Tasks

Task class is a blueprint for a task. Copy task comes pre-packaged and we can use it within our project after we :
- define an instance of that task class
- configure instance -> telling Copy about details like from and into.

Ad-hoc tasks combines task class and its definition at the same point. Example:

```groovy
tasks.register('sayHello') {
    doLast {
        println 'Hello'
    }
}
```

### Task creation

#### 1) Tasks.register

Best approach as it avoid unnecesary configuration. Class-based task (of Copy class):
See more about performance: https://docs.gradle.org/current/userguide/task_configuration_avoidance.html

```groovy
tasks.register('generateDescriptions', Copy) {
// configure task
}
```

#### 2) task

Uses [Project.task()](https://docs.gradle.org/current/javadoc/org/gradle/api/Project.html#task-java.lang.String-) method. Has worse performance

```groovy
task('generateDescriptions', type: Copy)
```

Example of class based task:
```groovy
task generateDescriptions(type: Copy) {
    from 'descriptions'
    into "$buildDir/descriptions"
    filter(ReplaceTokens, tokens: [THEME_PARK_NAME: "Grelephant's Wonder World"])
}
```

### Locating tasks

Configuration of already defined tasks.

#### 1) tasks.named

The best performance, recommended - it returns TaskProvider class instead of Task class. Perf benefits -- see book.

```groovy
tasks.named('generateDescriptions') {
    into "$buildDir/descriptions-renamed"
}
```

#### 2) tasks.getByName

Returns Task class -- slower.

```groovy
tasks.getByName('generateDescriptions') {
into "$buildDir/descriptions-renamed"
}
```

#### 3) tasks.\<taskName>

Do not have to work with all plugins.

```groovy
tasks.clean {
    doLast {
        println 'Squeaky clean!'
        
    }
}
```

#### 4) \<taskName>

In Groovy DSL it is possible to use configuration by using task name.
Unfortunately it also returns Task so there is perf downside.


```groovy
clean {
    doLast {
        println 'Squeaky clean!'
    }
}
```

### Task dependencies and ordering

- **dependsOn** prepareOutput - current task needs input from _prepareOutput_. Means that _prepareOutput_ will be executed automatically before the current task. 
- **mustRunAfter** zipAll - forces task order - current task must run after task B (it has effect of both tasks are actually going to take part int the build)
- **finalizedBy** taskA - taskA will be always executed after this task. TaskA will be executed even if the current task fails. Similar to finally section in try-catch. 

### Input and output linking

- https://docs.gradle.org/current/userguide/incremental_build.html#sec:link_output_dir_to_input_files
- https://docs.gradle.org/current/javadoc/org/gradle/api/tasks/TaskInputs.html

### Applying plugins

Recommended definition:

```groovy
plugins {
    id 'org.barfuin.gradle.taskinfo' version '1.3.1'
}
```

Legacy definition with missiong optimisations and IntelliJ IDEA broken integration:

```groovy
buildscript {
    repositories {
        maven {
            url "https://plugins.gradle.org/m2/"
        }
    }
    dependencies {
        classpath "gradle.plugin.org.barfuin.gradle.taskinfo:gradle-taskinfo:1.3.1"
    }
}
apply plugin: "org.barfuin.gradle.taskinfo"
```

How to search 3dr party plugins: https://plugins.gradle.org/ </br>
Core Gradle plugins: https://docs.gradle.org/current/userguide/plugin_reference.html

### Repositories and Dependencies

When searching for dependencies, repositories are used in provided order - declare the repo with the most dependencies first. 

```groovy
repositories {
    mavenCentral()
    google()
    maven {
        url 'https://my-custom-repo.com'
    }
}
```

Java classpath = list of files passed to Java when it complies and executes the code.
- Analogy in Gradle are compile and runtime classpaths.

Example how to declare runtime+compile time dependency with excluded transitive dependency.

```groovy
dependencies {
    implementation(group = "commons-beanutils", name = "commons-beanutils", version =
            "1.9.4") {
        exclude(group = "commons-collections", module = "commons-collections")
    }
}
```


# Tips

- Verbose console: `--console=verbose `
- Tasks default parameters (configuration like group, description, enabled) is [here](https://docs.gradle.org/current/javadoc/org/gradle/api/Task.html) - look for setters.
```groovy
tasks.register('sayBye') {
    doLast {
        println 'Bye!'
    }
    onlyIf {
        2 == 3 * 2
    }
}
```