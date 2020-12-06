package dev.paranoid

import androidx.compose.runtime.*
import dev.paranoid.ui.*

enum class Page {
    MAIN,
    CHAT
}

val images = listOf(R.drawable.ic_a, R.drawable.ic_b, R.drawable.ic_c, R.drawable.ic_d)

@Composable
fun MyApp() {

    var page by remember { mutableStateOf(Page.MAIN) }

    when (page) {
        Page.MAIN ->
            TabPage(
                tabCount = 4,
                tabTemplate = { TabIcon(images[it]) },
                viewTemplate = {
                    when (it) {
                        0 -> SwipePage()
                        1 -> NotImplemented()
                        2 -> ChatList(onClick = { page = Page.CHAT })
                        3 -> AboutMe()
                    }
                })
        Page.CHAT ->
            ChatPage(
                onExit = { page = Page.MAIN },
                messages = listOf(),
                onSent = { },
                name = ""
            )
    }
}

