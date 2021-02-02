package dev.paranoid.component

import androidx.compose.runtime.Composable
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.paranoid.android.MainActivity
import dev.paranoid.component.Profile
import dev.paranoid.theme.AppTheme
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@Composable
fun UserInput(onSend: (String) -> Unit) {
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
                    onSend(textState.text)
                    textState = TextFieldValue()
                }) {
                Text(text = "Send", modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}