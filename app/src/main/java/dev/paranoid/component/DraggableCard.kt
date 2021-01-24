package dev.paranoid.component

import android.os.Handler
import android.util.Log
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.unit.dp

@Composable
fun DraggableCard(
    modifier: Modifier = Modifier,
    onDisappear: (Boolean) -> Unit = {},
    doDisappear: Int,
    content: @Composable () -> Unit
) {

    var configuration = AmbientConfiguration.current
    var context = AmbientContext.current

    var down = Offset.Zero
    val x = animatedFloat(0f)
    val y = animatedFloat(0f)

    var onGoing by remember { mutableStateOf(false) }

    val letItGo = { targetX: Float ->
        if (!onGoing) {
            onGoing = true
            x.animateTo(targetX, tween(300))
            Handler(context.mainLooper).postDelayed({
                x.snapTo(0f)
                y.snapTo(0f)
                onDisappear(targetX > 0)
                onGoing = false
            }, 300)
        }
    }

    if (doDisappear > 0) {
        letItGo(2000f)
    } else if (doDisappear < 0) {
        letItGo(-1000f)
    }

    Card(
        modifier = modifier
            .dragGestureFilter(object : DragObserver {
                override fun onStart(downPosition: Offset) {
                    down = downPosition
                    super.onStart(downPosition)
                }

                override fun onStop(velocity: Offset) {
                    if (x.targetValue - down.x < 0) {
                        letItGo(-1000f)
                    } else if (x.targetValue - down.x > 0) {
                        letItGo(2000f)
                    }
                    super.onStop(velocity)
                }

                override fun onDrag(dragDistance: Offset): Offset {
                    x.animateTo(x.targetValue + dragDistance.x)
                    y.animateTo(y.targetValue + dragDistance.y)
                    return super.onDrag(dragDistance)
                }
            }, startDragImmediately = true)
            .rotate((x.value / 60).coerceIn(-30f, 30f))
            .offset(x.value.dp, y.value.dp)
            .clip(RoundedCornerShape(16.dp)),
        content = content
    )

}

