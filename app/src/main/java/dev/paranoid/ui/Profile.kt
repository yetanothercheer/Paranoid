package dev.paranoid.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.paranoid.data.TinderProfile

@Composable
fun ProfileCard(profile: TinderProfile) {
    Box() {
        Card(
            Modifier.fillMaxSize().clickable(onClick = {

            }),
            elevation = 10.dp
        ) {
            if (profile.avatars.isEmpty()) {
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
            asset = imageResource(id),
            modifier = Modifier.preferredSize(50.dp, 50.dp)
                .clickable(onClick = onClick)
        )
    }
}
