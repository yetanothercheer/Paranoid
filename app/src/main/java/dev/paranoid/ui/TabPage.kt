package dev.paranoid.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

@Composable
fun TabIcon(id: Int) {
    Image(
        vectorResource(id),
        modifier = Modifier.preferredSize(30.dp, 30.dp)
    )
}

@Composable
fun TabPage(
    tabCount: Int,
    currentTabIndex: Int,
    onCurrentTabIndexChanged: (Int) -> Unit,
    tabTemplate: @Composable (Int) -> Unit,
    viewTemplate: @Composable (Int) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        TabRow(
            modifier = Modifier.preferredHeight(60.dp).background(Color.White),
            selectedTabIndex = currentTabIndex,
        ) {
            (0 until tabCount).forEach {
                Tab(
                    modifier = Modifier.background(Color.White),
                    selected = currentTabIndex == it,
                    onClick = { onCurrentTabIndexChanged(it) }) {
                    tabTemplate(it)
                }
            }
        }

        viewTemplate(currentTabIndex)
    }
}