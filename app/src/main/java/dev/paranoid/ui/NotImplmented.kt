package dev.paranoid.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.paranoid.theme.AppTheme

@Composable
fun NotImplemented() {
    Surface {
        Text(
            modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
            text = "Not Implemented"
        )
    }
}

@Preview
@Composable
fun NotImplementedPreview() {
    AppTheme {
        NotImplemented()
    }
}