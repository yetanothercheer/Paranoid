package dev.paranoid.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.random.Random

fun String.toColor() = Color(0xff000000 + Random(hashCode()).nextLong(0xffffff))

@Composable
fun Profile(name: String, modifier: Modifier = Modifier, onDisappear: (Boolean) -> Unit = {}, doDisappear: Int = 0) {
    DraggableCard(modifier, onDisappear, doDisappear) {
        Surface(
            Modifier
                .fillMaxSize()
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .background(name.toColor())
                    .padding(20.dp)
            ) {
                Spacer(Modifier.weight(1f))
                Text(name, style = MaterialTheme.typography.h2.copy(color = Color.White))
            }
        }
    }
}

