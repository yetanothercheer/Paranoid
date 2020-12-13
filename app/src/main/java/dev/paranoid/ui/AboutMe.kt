package dev.paranoid.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.paranoid.viewmodel.ProfileViewModel

@Composable
fun AboutMe(profileViewModel: ProfileViewModel) {
    val profile by profileViewModel.myProfile.observeAsState()

    Box(Modifier.padding(10.dp)) {
        profile?.let {
            ProfileCard(it)
        }
    }
}