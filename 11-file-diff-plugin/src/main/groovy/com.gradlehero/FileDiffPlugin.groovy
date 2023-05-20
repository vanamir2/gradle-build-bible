package com.gradlehero

import org.gradle.api.Plugin
import org.gradle.api.Project

class FileDiffPlugin implements Plugin<Project> {
    void apply(Project project) {
        FileDiffPluginExtension extension = project.extensions.create('fileDiff', FileDiffPluginExtension)
        project.tasks.register('fileDiff', FileDiffTask) {
            file1 = extension.getFile1()
            file2 = extension.getFile2()
        }
    }
}