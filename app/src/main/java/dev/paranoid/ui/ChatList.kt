package dev.paranoid.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.paranoid.theme.AppTheme

data class CompactProfile(val name: String, val recentMessage: String)

typealias CompactProfileList = List<CompactProfile>

@Composable
fun ChatList(profiles: CompactProfileList = listOf(
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
    CompactProfile("Bob", "bye"),
), onClick: (CompactProfile) -> Unit = {}) {
    Surface {
        LazyColumnFor(items = profiles, Modifier.fillMaxSize()) {
            Row(Modifier.clickable(onClick = { onClick(it) })) {
                Box(Modifier.width(64.dp).height(64.dp))
                Column {
                    Text(text = it.name, style = MaterialTheme.typography.h2)
                    Text(text = it.recentMessage, style = MaterialTheme.typography.body1)
                    Spacer(Modifier.preferredHeight(32.dp))
                    Divider(Modifier.fillMaxWidth().preferredHeight(1.dp),
                        color = MaterialTheme.colors.onSurface.copy(0.3f))
                }
            }
        }
    }
}

@Preview(widthDp = 480)
@Composable
fun ChatListPreview() {
    AppTheme {
        ChatList(
            listOf(
                CompactProfile("Alice", "hi"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
                CompactProfile("Bob", "bye"),
            )
        )
    }
}