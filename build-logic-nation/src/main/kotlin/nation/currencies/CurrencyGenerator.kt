package nation.currencies

import nation.currencies.GeneratorDefaults.DEFAULT_PACKAGE_NAME
import groovy.json.JsonSlurper
import org.gradle.api.tasks.TaskAction
import java.io.File
import nation.countries.RawCountriesSource
import kotlinx.serialization.json.Json

abstract class CurrencyGenerator : AbstractGenerator() {

    private fun parseJson(json: String): Map<String, String> {
        val slurper = JsonSlurper()
        return slurper.parseText(json) as Map<String, String>
    }

    private val codec = Json {
        ignoreUnknownKeys = true
    }

    private fun generateCurrencies() {
        val output = File(outputDirWithPackage, "$clazz.kt")
        if (!output.exists()) output.createNewFile()
        val cr = CurrencyReader()
        val currencies = cr.getCurrencies()

        val countries = RawCountriesSource(codec).countries()

        output.writeText(
            """
            /*
             * This is a generated document
             * author of the generator: https://github.com/andylamax
             */
            // @file:JsExport
            @file:Suppress("unused","WRONG_EXPORTED_DECLARATION", "SERIALIZER_TYPE_INCOMPATIBLE")
            
            package $pkg
            
            import kotlin.jvm.JvmStatic
            import kotlin.js.JsExport
            import kotlinx.serialization.Serializable
            
            @Serializable(with = ISO3CurrencySerializer::class)
            enum class $clazz(val label: String, val code: String, val globalSymbol: String, val localSymbol: String, val country: Country,val lowestDenomination: Short) {
                // override fun toString() = name
                // companion object {
                //     @JvmStatic
                //     val values : Array<$clazz> by lazy { 
                //         ${currencies.joinToString(separator = ", ", prefix = "arrayOf(", postfix = ")") { it["cc"].toString() }}
                //     }
                //     @JvmStatic
                //     fun valueOf(currency: String) : $clazz = values.first { it.name == currency }
                //     
                //     @JvmStatic
                //     fun valueOfOrNull(currency: String?) : $clazz? = values.firstOrNull { it.name == currency }
                //     
                //     @JvmStatic
                //     fun valueOfOrDefault(currency: String?, default: $clazz) : $clazz = values.firstOrNull { it.name == currency } ?: default
                // }            
        """.trimIndent()
        )


        output.appendText("\n")
        val last = currencies.last()

        val unsupported = listOf(
            "West African CFA franc",
            "Special Drawing Rights",
            "East Caribbean dollar"
        )
        val codes = mapOf(
            "UXX" to "US",
            "XPF" to "FR",
            "EUR" to "GB",
            "ANG" to "NL",
            "XAF" to "CF"
        )
        for (entry in currencies) {
            val name = entry["name"]
            if (name in unsupported) continue
//            output.appendText("\n\t@Serializable(with = ISO3CurrencySerializer::class)\n")
            output.appendText("\t/**$name*/\n")
            val code = entry["cc"] ?: "UXX"
            val country = codes.getOrElse(code) { code.take(2) }
//            output.appendText("""${"\t"}data object ${entry["cc"]} : $clazz("$name","${entry["cc"]}","${symbol(entry["symbol"]!!)}","${symbol(entry["localSymbol"]!!)}","$name",${entry["lowestDenomination"]})""")
            output.appendText("""${"\t"}$code("$name","$code","${symbol(entry["symbol"]!!)}","${symbol(entry["localSymbol"]!!)}",Country.$country,${entry["lowestDenomination"]})""")
            if (entry == last) {
                output.appendText(";\n")
            } else {
                output.appendText(",\n")
            }
        }
        output.appendText("\n}")
    }

    @TaskAction
    fun execute() {
        outputDirWithPackage.mkdirs()
        generateCurrencies()
    }

    private fun symbol(input: String): String = when {
        input == "$" -> input
        input.endsWith("$") -> input
        else -> input.replace("$", """${"$"}{"$"}""")
    }
}