package dev.paranoid.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun RegisterScreen(onRegistered: () -> Unit = {}) {
    Surface(Modifier.fillMaxSize(), color = Color.Black) {
        Button(onRegistered) {
            Text("Register")
        }
    }
}
