package dev.paranoid

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import dev.paranoid.data.TinderViewModel
import dev.paranoid.theme.AppTheme
import dev.paranoid.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler

val handler = CoroutineExceptionHandler { _, exception ->
    Log.e("Coroutine Exception", "${exception.message} on ${Thread.currentThread()}")
}

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: TinderViewModel by viewModels()

        setContent {
            AppTheme {
                App(vm)
            }
        }
    }
}


