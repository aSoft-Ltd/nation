package nation.countries

import Resource
import kotlinx.serialization.StringFormat
import kotlinx.serialization.decodeFromString

class RawCountriesSource(private val codec: StringFormat) {
    fun countries(): List<RawCountry> {
        val json = Resource::class.java.getResource("countries.json").readText()
        val unsupportedCountries = listOf("Tonga", "Sao Tome and Principe")
        return codec.decodeFromString<Set<RawCountry>>(json).filterNot {
            unsupportedCountries.contains(it.name)
        }
    }
}