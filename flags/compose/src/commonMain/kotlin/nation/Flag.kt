@file:OptIn(ExperimentalResourceApi::class)

package nation

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import nation.generated.resources.Res
import nation.generated.resources.allDrawableResources
import nation.generated.resources.flag__unknown
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.vectorResource

@Composable
fun Flag(
    country: Country? = Country.entries.random(),
    modifier: Modifier = Modifier,
    alpha: Float = 1f,
    scale: ContentScale = ContentScale.FillWidth,
    alignment: Alignment = Alignment.Center,
) {
    val key = remember(country) { "flag_" + (country?.code?.lowercase() ?: "_unknown") }

    val resource = remember(key) { Res.allDrawableResources[key] ?: Res.drawable.flag__unknown }

    Image(
        imageVector = vectorResource(resource),
        contentDescription = "${country?.name ?: "Unknown"} flag",
        modifier = modifier,
        contentScale = scale,
        alignment = alignment,
        alpha = alpha
    )
}