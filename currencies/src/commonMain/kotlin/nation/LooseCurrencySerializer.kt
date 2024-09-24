package nation

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LooseCurrencySerializer : KSerializer<Currency> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("nation.Currency", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Currency) = encoder.encodeString(value.name)
    override fun deserialize(decoder: Decoder): Currency {
        val value = decoder.decodeString()
        val currencies = Currency.entries
        return currencies.firstOrNull {
            it.name == value
        } ?: currencies.firstOrNull {
            it.globalSymbol == value
        } ?: currencies.firstOrNull {
            it.localSymbol == value
        } ?: Currency.UXX
    }
}