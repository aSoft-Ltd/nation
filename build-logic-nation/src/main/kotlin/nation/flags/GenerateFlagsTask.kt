package nation.flags

import java.io.File
import nation.svg.XMLTokenizer
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

open class GenerateFlagsTask : DefaultTask() {
    @OutputDirectory
    val outputDir: File = File(project.buildDir, "generated/nation/flags/compose/kotlin")

    @InputDirectory
    val inputDir: File = project.file("../../build-logic-nation/src/main/resources/flags")

    @TaskAction
    fun doGeneration() {
        val input = inputDir
//        for (svg in input.listFiles()) svg.generateComposeFile()
    }

    fun File.generateComposeFile() {
        val tokenizer = XMLTokenizer(this)
        tokenizer.tokenize().toComposeFile(name.substringBefore("."))
    }

    fun XMLTokenizer.Tag.Svg.toComposeFile(name: String) {
        val file = File(outputDir, "flag_${name}.kt")
        file.writeText(
            """
            package nation
            import androidx.compose.ui.graphics.vector.ImageVector
            import androidx.compose.ui.graphics.vector.group
            import androidx.compose.ui.graphics.vector.path
            import androidx.compose.ui.unit.dp

            val Country.FLAG_${name.uppercase()} : ImageVector by lazy {
                if (_$name != null) {
			        return _$name!!
		        }

                _$name = ImageVector.Builder(
                    name = "Flag_${name.uppercase()}",
                    defaultWidth = ${view.w}.dp,
                    defaultHeight = ${view.h}.dp,
                    viewportWidth = ${view.w}f,
                    viewportHeight = ${view.h}f
                ).apply {
                    ${
                content.joinToString("\n") { tag ->
                    when (tag) {
                        is XMLTokenizer.Tag.Group -> """
                                group {
                                    ${
                            tag.children.joinToString("\n") { child ->
                                when (child) {
                                    is XMLTokenizer.Tag.Path -> "path(pathData = \"${child.d}\")"
                                    else -> ""
                                }
                            }
                        }
                                }
                            """

                        else -> ""
                    }
                }
            }
                }
            }
            
            private var _$name: ImageVector? = null
            """.trimIndent()
        )
    }

    private fun XMLTokenizer.Tag.Svg.toComposeString(name: String): String {
        val text = """
            import nation

            val Country.FLAG_${name.uppercase()} : ImageVector by lazy {
                if (_$name != null) {
			        return@lazy  _$name!!
		        }

                _$name = ImageVector.Builder(
                    name = "Flag_${name.uppercase()}",
                    defaultWidth = ${view.w}.dp,
                    defaultHeight = ${view.h}.dp,
                    viewportWidth = ${view.w}f,
                    viewportHeight = ${view.h}f
                ).apply {
                   ${content.map { it.toComposeString(indent = 2) }}
                }.build()
                
                _$name!!
            }
            
            private var _$name: ImageVector? = null
            """.trimIndent()
        return text
    }

    private fun XMLTokenizer.Tag.Path.toComposeString(indent: Int) = buildString {
        val tab = "    ".repeat(indent)
        appendLine("${tab}path(pathData = \"$d\")")
    }

    private fun XMLTokenizer.Tag.Group.toComposeString(indent: Int): String = buildString {
        val tab = "    ".repeat(indent)
        appendLine("${tab}group {")
        children.forEach { child ->
            appendLine(child.toComposeString(indent + 1))
        }
        appendLine("}")
    }

    private fun XMLTokenizer.Tag.toComposeString(indent: Int) = when (this) {
        is XMLTokenizer.Tag.Svg -> toComposeString(name.substringBefore("."))
        is XMLTokenizer.Tag.Path -> toComposeString(indent)
        is XMLTokenizer.Tag.Group -> toComposeString(indent)
    }
}