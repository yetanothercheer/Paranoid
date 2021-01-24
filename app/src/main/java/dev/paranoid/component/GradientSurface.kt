package dev.paranoid.component

import android.util.Log
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.layout.layout
import kotlin.random.Random

val random = Random(0)

@Composable
fun GradientSurface(modifier: Modifier = Modifier) {
    var width = 0f
    var height = 0f

    val r1 by remember {  mutableStateOf(random.nextFloat() % 1f) }
    val r2 by remember {  mutableStateOf(random.nextFloat() % 1f) }
    val r3 by remember {  mutableStateOf(random.nextFloat() % 1f) }

    val c = animatedFloat(initVal = 0f)

    if (c.value > 0.9f) {
        c.animateTo(0f, tween(10000))
    }
    if (c.value < 0.1f) {
        c.animateTo(1f, tween(10000))
    }

    Box(
        modifier
            .layout { measurable, constraints ->
                val placeable = measurable.measure(constraints)
                Log.d("COmpose", "Layout ${placeable.width} ${placeable.height}")
                width = placeable.width.toFloat()
                height = placeable.height.toFloat()
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            }
            .drawBehind {
                Log.d("Compsoe", "Draw")
                drawRect(
                    LinearGradient(
                        listOf(Color(0xffffffff), Color(c.value, c.value, c.value)),
                        -100f,
                        -300f,
                        width,
                        height
                    ), Offset.Zero, Size(width, height)
                )
            })
}
