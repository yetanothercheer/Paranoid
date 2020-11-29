package dev.paranoid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import dev.paranoid.R
import dev.paranoid.data.TinderProfile
import dev.paranoid.data.TinderViewModel

@Composable
fun App(vm: TinderViewModel) {
    var state by remember { mutableStateOf(0) }

    val myProfile by vm.myProfile.observeAsState(TinderProfile("", "", "", listOf(), listOf()))
    val allThePeople by vm.allThePeople.observeAsState(listOf())
    val matches by vm.matches.observeAsState(listOf())
    val messages by vm.messages.observeAsState(listOf())

    // Chat Person
    var b by remember { mutableStateOf("") }

    var fullScreenAt by remember { mutableStateOf(0) }

    when (fullScreenAt) {
        0 ->
            Column(Modifier.fillMaxSize()) {
                TabRow(
                    modifier = Modifier.preferredHeight(60.dp).background(Color.White),
                    selectedTabIndex = state
                ) {
                    val images =
                        listOf(R.drawable.ic_a, R.drawable.ic_b, R.drawable.ic_c, R.drawable.ic_d)

                    images.forEachIndexed { index, title ->
                        Tab(
                            modifier = Modifier.background(Color.White),
                            selected = state == index,
                            onClick = { state = index }) {
                            Image(
                                asset = vectorResource(images[index]),
                                modifier = Modifier.preferredSize(30.dp, 30.dp)
                            )
                        }
                    }
                }

                var index by remember { mutableStateOf(0) }
                var chatting by remember { mutableStateOf(-1) }
                when (state) {
                    0 -> when (allThePeople.size) {
                        0 -> Text(
                            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                            text = "Loading..."
                        )
                        else -> when (index < allThePeople.size) {
                            true ->
                                Column {
                                    Box(
                                        modifier = Modifier.weight(1f).fillMaxWidth().padding(7.dp)
                                    ) {
                                        ProfileCard(allThePeople[index])
                                    }
                                    Row(
                                        Modifier.padding(15.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        MyButton(R.drawable.notlike) { index++ }
                                        Spacer(modifier = Modifier.width(50.dp))
                                        MyButton(R.drawable.like) { vm.like(allThePeople[index].id.toInt()); index++ }
                                    }
                                }
                            else -> Text(
                                modifier = Modifier.fillMaxSize()
                                    .wrapContentSize(Alignment.Center),
                                text = "There's no one left."
                            )
                        }
                    }
                    1 -> Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        text = "Not Implemented"
                    )
                    2 -> Column(Modifier.padding(10.dp)) {
                        LazyColumnFor(items = matches) {
                            Column {
                                Card(Modifier.fillMaxWidth().clickable(onClick = {
                                    chatting = it.id.toInt()
                                    b = it.id
                                    vm.getChatsWith(it.id)
                                    fullScreenAt = 1
                                })) {
                                    Text(
                                        "${it.name} with ID: ${it.id}",
                                        Modifier.padding(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                        }
                    }
                    3 -> Box(Modifier.padding(10.dp)) {
                        ProfileCard(myProfile)
                    }
                }
            }
        1 ->
            ChatPage(
                onExit = { vm.exitChats(); fullScreenAt = 0 },
                messages = messages,
                onSent = { vm.talk(b, it) },
                name = b
            )
    }
}

