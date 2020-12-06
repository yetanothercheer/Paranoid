package dev.paranoid.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.paranoid.R
import dev.paranoid.data.TinderProfile
import dev.paranoid.theme.AppTheme

@Composable
fun SwipePage(
    profiles: List<TinderProfile> = listOf(
        TinderProfile("", "Cheer1", "About...", listOf(), listOf()),
        TinderProfile("", "Cheer2", "About...", listOf(), listOf()),
        TinderProfile("", "Cheer3", "About...", listOf(), listOf()),
    ), onLike: () -> Unit = {}, onDislike: () -> Unit = {}
) {

    var current by remember { mutableStateOf(0) }

    Surface {
        when (profiles.size) {
            0 -> Text(
                modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                text = "Loading..."
            )
            else ->
                when (current < profiles.size) {
                    true ->
                        Column {
                            Box(
                                modifier = Modifier.weight(1f).fillMaxWidth().padding(7.dp)
                            ) {
                                ProfileCard(profiles[current])
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
        }
    }
}

@Preview
@Composable
fun SwipePagePreview() {
    AppTheme {
        SwipePage(
            listOf(
                TinderProfile("", "Cheer", "About...", listOf(), listOf())
            )
        )
    }
}