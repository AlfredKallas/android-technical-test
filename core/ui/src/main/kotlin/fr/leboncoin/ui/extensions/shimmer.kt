package fr.leboncoin.ui.extensions

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

@Suppress("AssignedValueIsNeverRead")
@Composable
private fun Modifier.shimmer(
    cornerRadius: Dp = 0.dp,
    durationMillis: Int = 1000
): Modifier = composed {

    var elementSize by remember {
        mutableStateOf(IntSize.Zero)
    }

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 1.0f),
        Color.LightGray.copy(alpha = 0.2f),
    )

    val transition = rememberInfiniteTransition()
    val translateAnim by transition.animateFloat(
        initialValue = -2 * elementSize.width.toFloat(),
        targetValue = 2 * elementSize.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis, // slower = smoother
                easing = FastOutSlowInEasing // smoother easing
            )
        )
    )

    this.drawWithCache {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim, 0f),
            // wider gradient
            end = Offset(translateAnim + size.width, size.height)
        )
        val cornerPx = cornerRadius.toPx()
        onDrawBehind {
            drawRoundRect(
                brush = brush,
                cornerRadius = CornerRadius(cornerPx, cornerPx),
                size = size
            )
        }
    }.onGloballyPositioned{
        elementSize = it.size
    }
}

@Composable
fun Modifier.shimmer(
    isLoading: Boolean,
    cornerRadius: Dp = 0.dp,
    durationMillis: Int = 2000
): Modifier {
    return if (isLoading)
        this.shimmer(
            cornerRadius = cornerRadius,
            durationMillis = durationMillis)
    else this
}