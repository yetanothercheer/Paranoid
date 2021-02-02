package dev.paranoid.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.paranoid.data.repository.User

@Composable
fun ProfileScreen(users: List<User>, onLike: (String) -> Unit = {}) {
    Column(Modifier.fillMaxSize()) {
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
                        if (it) onLike(users[swpieIndex % users.size].id)
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
                .preferredHeight(70.dp),
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