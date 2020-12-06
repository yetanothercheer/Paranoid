package dev.paranoid.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.paranoid.data.TinderProfile

@Composable
fun AboutMe() {
    Box(Modifier.padding(10.dp)) {
        ProfileCard(TinderProfile("", "Cheer", "About...", listOf(), listOf()))
    }
}