package dev.paranoid.ui

import android.util.Log
import androidx.compose.animation.DpPropKey
import androidx.compose.animation.OffsetPropKey
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.DragObserver
import androidx.compose.ui.gesture.dragGestureFilter
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.paranoid.MainActivity
import dev.paranoid.data.repository.Profile
import kotlin.math.abs

@Composable
fun ProfileCard(profile: Profile, onDisappear: () -> Unit = {}) {
    var start by remember { mutableStateOf(Offset.Zero) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box (Modifier
            .fillMaxSize()
            .absoluteOffset(
                    (offset.x / MainActivity.density).dp,
                    (offset.y / MainActivity.density).dp)
            // TODO: Replace magic number with computed value
            .rotate((if (start.y < 600) 1 else -1) * offset.x / 180f)
    ){
        Surface(
                Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(7.dp))
                        .dragGestureFilter(
                                object : DragObserver {
                                    override fun onStart(downPosition: Offset) {
                                        start = downPosition
                                        super.onStart(downPosition)
                                    }

                                    override fun onDrag(dragDistance: Offset): Offset {
                                        offset += dragDistance
                                        return super.onDrag(dragDistance)
                                    }

                                    override fun onStop(velocity: Offset) {
                                        val final = start + offset
                                        if (abs(offset.x) > 500) {
                                            onDisappear()
                                        }
                                        offset = Offset.Zero
                                        super.onStop(velocity)
                                    }
                                },
                                startDragImmediately = true
                        ),
                elevation = 10.dp) {
                Canvas(Modifier.fillMaxSize(), onDraw = {
                    drawRect(
                        RadialGradient(
                            listOf(Color.Red, Color.Yellow),
                            -100f,
                            100f,
                            2000f
                        )
                    )
                })
        }
        Column(Modifier.align(Alignment.BottomStart).padding(10.dp)) {
            Text(
                profile.name,
                style = TextStyle(fontSize = 50.sp, color = Color.White)
            )
            if (profile.about.isNotEmpty()) {
                Text(
                    profile.about,
                    style = TextStyle(fontSize = 30.sp, color = Color.White)
                )
            }
        }
    }
}

@Composable
fun MyButton(id: Int, onClick: () -> Unit) {
    Card(elevation = 4.dp, shape = RoundedCornerShape(50)) {
        Image(
            imageResource(id),
            modifier = Modifier.preferredSize(50.dp, 50.dp)
                .clickable(onClick = onClick)
        )
    }
}

@Preview
@Composable
fun ProfilePreview() {
}
