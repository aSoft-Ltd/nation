import nation.countries.GenerateCountriesTask
import nation.languages.GenerateLanguagesTask
import org.gradle.kotlin.dsl.registering

val generateCountries by tasks.registering(GenerateCountriesTask::class)
