package ch.raffael.doclets.pegdown.integrations.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.javadoc.Javadoc

class PegdownDocletPlugin implements Plugin<Project> {
    final def CONFIGURATION_NAME = "pegdownDoclet"

    void apply(Project project) {
        // create a new configuration for the doclet dependency
        def config = project.configurations.create(CONFIGURATION_NAME)

        // add the doclet dependency
        def pegdownVersion = new InputStreamReader(PegdownDocletPlugin.class.getResourceAsStream('version.txt')).withReader { reader ->
            reader.text.trim()
        }
        project.dependencies.add(CONFIGURATION_NAME, "ch.raffael.pegdown-doclet:pegdown-doclet:$pegdownVersion")

        // after the user buildscript is evaluated ...
        project.gradle.projectsEvaluated {

            // ... adjust the javadoc tasks
            project.tasks.withType Javadoc.class, {
                it.options {
                    docletpath = config.files.asType(List)
                    doclet = "ch.raffael.doclets.pegdown.PegdownDoclet"
                    addStringOption("parse-timeout", "10")
                }
            }
        }
    }
}
