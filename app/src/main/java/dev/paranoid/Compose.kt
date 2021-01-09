package dev.paranoid

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import dev.paranoid.data.repository.Chat
import dev.paranoid.theme.AppTheme
import dev.paranoid.ui.*
import dev.paranoid.viewmodel.ChatViewModel
import dev.paranoid.viewmodel.ProfileViewModel

enum class Page {
    MAIN,
    CHAT
}

val images = listOf(R.drawable.ic_a, R.drawable.ic_b, R.drawable.ic_c, R.drawable.ic_d)

@Composable
fun App(chatViewModel: ChatViewModel, profileViewModel: ProfileViewModel) {

    var page by remember { mutableStateOf(Page.MAIN) }
    var tab by remember { mutableStateOf(0) }

    val chat by chatViewModel.current.observeAsState(Chat("", "", listOf()))

    when (page) {
        Page.MAIN ->
            TabPage(
                tabCount = 4,
                currentTabIndex = tab,
                onCurrentTabIndexChanged = { tab = it },
                tabTemplate = { TabIcon(images[it]) },
                viewTemplate = {
                    when (it) {
                        0 -> SwipePage(profileViewModel)
                        1 -> NotImplemented()
                        2 -> ChatList(chatViewModel, onClick = { page = Page.CHAT })
                        3 -> AboutMe(profileViewModel)
                    }
                })
        Page.CHAT ->
            ChatPage(
                messages = chat.messages,
                onExit = { page = Page.MAIN },
                onSent = { },
                name = ""
            )
    }
}

@Composable
fun Compose(chatViewModel: ChatViewModel, profileViewModel: ProfileViewModel) {
    AppTheme {
        App(chatViewModel, profileViewModel)
    }
}
