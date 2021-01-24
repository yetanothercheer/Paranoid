package dev.paranoid

import android.view.View
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.paranoid.android.MainActivity
import dev.paranoid.component.*
import dev.paranoid.data.repository.ChatRepository
import dev.paranoid.data.repository.User
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

    val data: String by inject()

    val chatRepo by inject<ChatRepository>()
    val userRepo by inject<UserRepository>()

    @Composable
    fun Main() {

        val context = AmbientContext.current
        var dark by remember { mutableStateOf(false) }

        var page by remember { mutableStateOf(0) }
        var index by remember { mutableStateOf(0) }

        var talkTo by remember { mutableStateOf("") }

        // Preventing collect flow everytime
        val usersGlobal = userRepo.getRecommends()

        AppTheme(dark) {

            val color = MaterialTheme.colors.background

            onCommit {
                (context as MainActivity).window.apply {
                    statusBarColor = Color.White.toArgb()
                    navigationBarColor = Color.White.toArgb()
                    decorView.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                }
            }

            var tabIndex by remember { mutableStateOf(0) }

            when (page) {

                0 -> Scaffold(
                    modifier = Modifier.semantics { testTag = "???" },
                    topBar = {
                        TabRow(
                            selectedTabIndex = tabIndex,
                            modifier = Modifier.preferredHeight(64.dp),
                            indicator = {},
                        ) {
                            (0..1).forEach {
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
                                    }
                                }
                            }
                        }
                    },
                    bodyContent = {
                        when (tabIndex) {
                            0 -> {
                                Column(Modifier.fillMaxSize()) {

                                    val users by usersGlobal.collectAsState(initial = listOf())
                                    var swpieIndex by remember { mutableStateOf(0) }

                                    // TODO: Replace this brittle variable
                                    var doDisappear by remember { mutableStateOf(0) }

                                    Box(
                                        Modifier
                                            .weight(1f)
                                            .zIndex(1f)
                                            .padding(5.dp)
                                    ) {
                                        if (users.isNotEmpty()) {
                                            Profile(users[(swpieIndex + 1) % users.size].name)
                                            Profile(
                                                users[swpieIndex % users.size].name,
                                                onDisappear = {
                                                    if (it) userRepo.like(users[swpieIndex % users.size].id)
                                                    doDisappear = 0
                                                    swpieIndex++
                                                },
                                                doDisappear = doDisappear
                                            )
                                        }
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .preferredHeight(100.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        val RoundButton =
                                            @Composable { icon: ImageVector, color: Color, onClick: () -> Unit ->
                                                Surface(
                                                    modifier = Modifier,
                                                    elevation = 7.dp,
                                                    shape = RoundedCornerShape(50)
                                                ) {
                                                    IconButton(onClick) {
                                                        Image(
                                                            icon,
                                                            colorFilter = ColorFilter.tint(color)
                                                        )
                                                    }
                                                }
                                            }
                                        RoundButton(
                                            Icons.Filled.Clear,
                                            Color.Red.copy(0.7f)
                                        ) { doDisappear = -1 }
                                        Spacer(Modifier.width(80.dp))
                                        RoundButton(
                                            Icons.Filled.Check,
                                            Color.Green.copy(0.7f)
                                        ) { doDisappear = 1 }
                                    }
                                }
                            }
                            1 -> {
                                ScrollableColumn(
                                    Modifier
                                        .fillMaxSize()
                                ) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        "Matched People",
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 5.dp
                                        ),
                                        style = MaterialTheme.typography.h6.copy(MaterialTheme.colors.secondary)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

                                    //
                                    ScrollableRow() {
                                        // val users by userRepo.getMatches().collectAsState(initial = listOf())
                                        val users = listOf<User>()

                                        users.forEach {
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Surface(
                                                Modifier
                                                    .height(70.dp)
                                                    .width(70.dp)
                                                    .clip(
                                                        RoundedCornerShape(50)
                                                    ),
                                                color = it.name.toColor()
                                            ) {}
                                        }
                                        Spacer(modifier = Modifier.width(10.dp))
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        "Chats",
                                        modifier = Modifier.padding(
                                            horizontal = 10.dp,
                                            vertical = 5.dp
                                        ),
                                        style = MaterialTheme.typography.h6.copy(MaterialTheme.colors.secondary)
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

//                                    val users by userRepo.getChats().collectAsState(initial = listOf())
                                    val users by usersGlobal.collectAsState(initial = listOf())

                                    LazyColumnFor(
                                        items = users,
                                        Modifier
                                            .fillMaxWidth()
                                            .weight(1f)
                                    ) {
                                        Surface(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(100.dp)
                                                .clickable {
                                                    page = 1
                                                    talkTo = it.name
                                                }) {
                                            Row(
                                                Modifier
                                                    .fillMaxSize()
                                                    .padding(start = 10.dp, top = 5.dp)
                                            ) {
                                                Surface(
                                                    Modifier
                                                        .height(70.dp)
                                                        .width(70.dp)
                                                        .clip(
                                                            RoundedCornerShape(50)
                                                        ),
                                                    color = it.name.toColor()
                                                ) {}
                                                Column(Modifier.fillMaxHeight()) {
                                                    Column(
                                                        modifier = Modifier
                                                            .padding(horizontal = 10.dp)
                                                            .weight(1f)
                                                    ) {
                                                        Text(
                                                            text = it.name,
                                                            style = MaterialTheme.typography.h5
                                                        )
                                                        Text(
                                                            text = it.lastMessage,
                                                            style = MaterialTheme.typography.body1
                                                        )
                                                    }
                                                    Divider()
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                )

                1 -> Scaffold(
                    topBar = {
                        TopAppBar(
                            modifier = Modifier
                                .clickable { page = 0 }
                                .height(64.dp),
                            title = {
                                Text(talkTo)
                            },
                            navigationIcon = {
                                IconButton(onClick = { page = 0 }) {
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
                            val primaryColor = MaterialTheme.colors.primary

                            onCommit {
                                (context as MainActivity).window.apply {
                                    statusBarColor = primaryColor.copy(0.7f).toArgb()
                                    decorView.systemUiVisibility =
                                        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                                }
                            }

                            val messages by chatRepo.getChatsWith(talkTo)
                                .map { it.map { Message(it.message, it.from != talkTo) } }
                                .collectAsState(listOf<Message>())

                            println("$talkTo : $messages")

                            Conversation(messages, Modifier.weight(1f))
                            UserInput(onSend = {
                                // TODO: Replace GlobalScope
                                GlobalScope.launch {
                                    chatRepo.chat(talkTo, it).collect {
//                                        Handler(context.mainLooper).post {
//                                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//                                        }
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
