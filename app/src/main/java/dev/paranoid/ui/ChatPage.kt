package dev.paranoid.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.paranoid.R
import dev.paranoid.data.repository.Message
import dev.paranoid.theme.AppTheme

@Composable
fun ChatPage(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    onSent: (String) -> Unit,
    onExit: () -> Unit,
    name: String
) {
    val scrollState = rememberScrollState()
    Surface(modifier = Modifier) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize()) {
                Messages(Modifier.weight(1f), scrollState, messages = messages)
                UserInput(onMessageSent = onSent)
            }
            ChatAppBar(onNavIconPressed = onExit, title = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(name, textAlign = TextAlign.Center)
                }
            }, actions = {
                Icon(
                    Icons.Outlined.Info,
                    modifier = Modifier.clickable(onClick = {})
                        .padding(horizontal = 12.dp, vertical = 16.dp).preferredHeight(24.dp)
                )
            })
        }
    }
}

@Composable
fun ChatAppBar(
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = {},
    title: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val backgroundColor = MaterialTheme.colors.surface
    Column {
        TopAppBar(
            modifier = modifier,
            backgroundColor = backgroundColor.copy(alpha = 0.95f),
            title = { Row { title() } },
            elevation = 0.dp,
            contentColor = MaterialTheme.colors.onSurface,
            navigationIcon = {
                Icon(
                    vectorResource(R.drawable.ic_back),
                    modifier = Modifier.fillMaxHeight().clickable(onClick = onNavIconPressed)
                        .padding(horizontal = 6.dp, vertical = 12.dp).scale(0.6f)
                )
            },
            actions = actions
        )
        Divider()
    }
}

@Composable
fun Messages(
    modifier: Modifier = Modifier,
    scrollState: ScrollState,
    messages: List<Message>
) {
    Box(modifier) {
        ScrollableColumn(
            scrollState = scrollState,
            reverseScrollDirection = true,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.preferredHeight(64.dp))
            messages.forEach {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    if (it.from == "myself") {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Text(it.body)
                }
            }
        }
    }
}

@Composable
fun UserInput(onMessageSent: (String) -> Unit) {
    Column {
        Divider()
        Row(
            Modifier.padding(horizontal = 4.dp).wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var textState by remember { mutableStateOf(TextFieldValue()) }
            var sendMessageEnabled by remember { mutableStateOf(false) }

            BasicTextField(modifier = Modifier.weight(1f).padding(
                horizontal = 4.dp, vertical = 8.dp
            ), value = textState, onValueChange = {
                textState = it
                sendMessageEnabled = it.text.isNotEmpty()
            })

            val border = if (!sendMessageEnabled) {
                BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
            } else {
                null
            }

            Button(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    .preferredHeight(36.dp),
                colors = ButtonConstants.defaultButtonColors(
                    disabledBackgroundColor = MaterialTheme.colors.surface,
                    disabledContentColor = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
                ),
                enabled = sendMessageEnabled,
                border = border,
                shape = RoundedCornerShape(50),
                onClick = {
                    onMessageSent(textState.text)
                }) {
                Text(text = "Send", modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}


@Preview(widthDp = 480)
@Composable
fun ChatPreview() {
    AppTheme {
        ChatPage(messages = listOf(), onSent = {}, onExit = {}, name = "Test")
    }
}