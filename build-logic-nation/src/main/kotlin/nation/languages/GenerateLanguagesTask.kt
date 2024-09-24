package nation.languages

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

open class GenerateLanguagesTask : DefaultTask() {

    @TaskAction
    fun doGeneration() {
        println("Generating")
    }
}