package dev.paranoid

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import dev.paranoid.android.MainActivity
import dev.paranoid.component.*
import dev.paranoid.data.repository.ChatRepository
import dev.paranoid.data.repository.UserRepository
import dev.paranoid.theme.AppTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@KoinApiExtension
class App(val toast: (String) -> Unit = {}) : KoinComponent {

    val chatRepo by inject<ChatRepository>()
    val userRepo by inject<UserRepository>()

    @Composable
    fun Main() {

        val appNavController = rememberNavController()
        val mainNavController = rememberNavController()

        val usersFlow = userRepo.getRecommends()

        AppTheme {
            var tabIndex by remember { mutableStateOf(0) }

            NavHost(appNavController, "loading") {

                composable("loading") {
                    LoadingScreen {
                        appNavController.navigate("main")
                    }
                }

                composable("register") {
                    RegisterScreen()
                }

                composable("main") {

                    val context = AmbientContext.current

                    onActive {
                        (context as MainActivity).window.apply {
                            statusBarColor = Color.White.toArgb()
                            navigationBarColor = Color.White.toArgb()
                            decorView.systemUiVisibility =
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                        }
                    }

                    Scaffold(
                        topBar = {
                            TabRow(
                                selectedTabIndex = tabIndex,
                                modifier = Modifier.preferredHeight(64.dp),
                                indicator = {},
                            ) {
                                (0..2).forEach {
                                    val selected = tabIndex == it
                                    Tab(
                                        selected,
                                        modifier = Modifier.background(Color.White),
                                        onClick = { tabIndex = it }) {

                                        val GradientIcon =
                                            @Composable { image: ImageVector, colorful: Boolean ->
                                                Image(
                                                    image,
                                                    colorFilter = ColorFilter.tint(if (colorful) MaterialTheme.colors.primary else Color.Gray)
                                                )
                                            }

                                        when (it) {
                                            0 -> GradientIcon(Icons.Filled.Favorite, selected)
                                            1 -> GradientIcon(Icons.Filled.Call, selected)
                                            2 -> GradientIcon(Icons.Filled.Home, selected)
                                        }
                                    }
                                }
                            }
                        },
                        bodyContent = {
                            val users by usersFlow.collectAsState(listOf())

                            when (tabIndex) {
                                0 -> {
                                    ProfileScreen(users) { }
                                }
                                1 -> {
                                    ChatListScreen(users) { appNavController.navigate("chat/$it") }
                                }
                                2 -> {
                                    MyProfileScreen()
                                }
                            }
                        }
                    )

                }
                composable(
                    "chat/{id}",
                    arguments = listOf(navArgument("id") { defaultValue = "me" })
                ) {
                    val id = it.arguments!!.getString("id")!!

                    Scaffold(
                        topBar = {
                            TopAppBar(
                                modifier = Modifier.height(64.dp),
                                title = { Text(id) },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        appNavController.navigate("main")
                                    }) {
                                        Image(
                                            Icons.Filled.ArrowBack,
                                            colorFilter = ColorFilter.tint(MaterialTheme.colors.onPrimary)
                                        )
                                    }
                                },
                            )
                        },
                        bodyContent = {
                            Column {

                                val context = AmbientContext.current
                                val primaryColor = MaterialTheme.colors.primary

                                onCommit {
                                    (context as MainActivity).window.apply {
                                        statusBarColor = primaryColor.copy(0.7f).toArgb()
                                        decorView.systemUiVisibility =
                                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                                    }
                                }

                                val messages by chatRepo.getChatsWith(id)
                                    .map { it.map { Message(it.message, it.from != id) } }
                                    .collectAsState(listOf<Message>())

                                Conversation(messages, Modifier.weight(1f))
                                UserInput(onSend = {
                                    GlobalScope.launch {
                                        chatRepo.chat(id, it).collect {
                                            println(it)
                                        }
                                    }
                                })
                            }
                        }
                    )
                }
            }

        }
    }

}
