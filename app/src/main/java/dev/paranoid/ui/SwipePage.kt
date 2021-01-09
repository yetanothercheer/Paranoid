package dev.paranoid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.paranoid.R
import dev.paranoid.viewmodel.ProfileViewModel

@Composable
fun SwipePage(
    profileViewModel: ProfileViewModel, onLike: () -> Unit = {}, onDislike: () -> Unit = {}
) {
    val hasProfile by profileViewModel.hasProfile.observeAsState(false)
    val profiles by profileViewModel.profiles.observeAsState(listOf())

    var current by remember { mutableStateOf(0) }

    Surface {
        if (hasProfile) {
            when (current < profiles.size) {
                true ->
                    Column {
                        Box(Modifier.weight(1f).fillMaxSize()) {
                            Box(
                                    modifier = Modifier.fillMaxWidth().padding(7.dp)
                            ) {
                                ProfileCard(profiles[current], onDisappear = { current++ })
                            }
                            if (current < profiles.size - 1) {
                                Box(
                                        modifier = Modifier.fillMaxWidth().padding(7.dp)
                                ) {
                                    ProfileCard(profiles[current + 1], onDisappear = { current++ })
                                }
                            }
                        }
                        Row(
                            Modifier.padding(15.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            MyButton(R.drawable.notlike) { current++ }
                            Spacer(modifier = Modifier.width(50.dp))
                            MyButton(R.drawable.like) { current++ }
                        }
                    }
                else -> Text(
                    modifier = Modifier.fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    text = "There's no one left."
                )
            }
        } else {
            Text(
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                text = "Loading..."
            )
        }
    }
}

@Preview
@Composable
fun SwipePagePreview() {
}
