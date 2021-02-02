package dev.paranoid.component

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import dev.paranoid.data.repository.User

@Composable
fun ChatListScreen(users: List<User>, onSelected: (String) -> Unit = {}) {
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
                        onSelected(it.name)
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

