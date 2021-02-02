package dev.paranoid.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.WindowManager
import androidx.compose.ui.platform.setContent
import dev.paranoid.App
import dev.paranoid.R
import dev.paranoid.data.SeriousRepository
import dev.paranoid.data.SimpleRepository
import dev.paranoid.data.db.AppDatabase
import dev.paranoid.data.repository.ChatRepository
import dev.paranoid.data.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.binds
import org.koin.dsl.module

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidLogger()
            androidContext(this@MainActivity)

            // _modules_ is a must
            modules(
                module {
                    val db = AppDatabase.getInstance(this@MainActivity)

                    single {
                        SeriousRepository(
                            db.userDao,
                            db.chatDao,
                            this@MainActivity
                        )
                    } binds arrayOf(ChatRepository::class, UserRepository::class)
                },
            )
        }

        setContent {
            App().Main()
        }
    }
}
