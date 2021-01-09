package dev.paranoid

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import dev.paranoid.viewmodel.ChatViewModel
import dev.paranoid.viewmodel.ProfileViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.android.viewmodel.ext.android.viewModel

val handler = CoroutineExceptionHandler { _, exception ->
    Log.e("Coroutine Exception", "${exception.message} on ${Thread.currentThread()}")
}

class MainActivity : AppCompatActivity() {
    companion object {
        // DPI Density
        // TODO: Find a better way to use Context in composable
        var density = 0f
    }

    private val chatViewModel: ChatViewModel by viewModel()
    private val profileViewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        density = resources.displayMetrics.density
        setContent { Compose(chatViewModel, profileViewModel) }
    }
}
