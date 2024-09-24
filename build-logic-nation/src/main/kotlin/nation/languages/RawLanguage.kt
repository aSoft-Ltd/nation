package nation.languages

import kotlinx.serialization.Serializable

@Serializable
data class RawLanguage(
    val name: String,
    val native: String
)