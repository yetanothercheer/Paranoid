package dev.paranoid.component

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Message(val body: String, val isFromMe: Boolean)

@Composable
fun Conversation(
    messages: List<Message>,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()

    Surface(modifier) {
        ScrollableColumn(
            scrollState = scrollState,
            reverseScrollDirection = true,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.preferredHeight(32.dp))
            messages.forEach {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    if (it.isFromMe) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Surface(
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            it.body,
                            Modifier
                                .background(Color(0.9f, 0.9f, 0.9f))
                                .padding(vertical = 5.dp, horizontal = 10.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.preferredHeight(32.dp))
        }
    }
}
