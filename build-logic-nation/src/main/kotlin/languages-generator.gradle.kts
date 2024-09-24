import nation.languages.GenerateLanguagesTask
import org.gradle.kotlin.dsl.registering

val generateLanguages by tasks.registering(GenerateLanguagesTask::class)
