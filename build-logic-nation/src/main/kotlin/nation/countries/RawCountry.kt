package nation.countries

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawCountry(
    val name: String,
    val code: String,
    val currency: RawCurrency,
    val language: RawLanguage,
    val flag: String,
    @SerialName("dialling_code") val dialingCode: String,
    val isoCode: String
)

@Serializable
data class RawCurrency(
    val code: String,
    val name: String,
//    val symbol: String,
)

@Serializable
data class RawLanguage(
//    val code: String,
    val name: String
)