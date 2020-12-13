package dev.paranoid

import android.app.Application
import dev.paranoid.data.SimpleChatRepository
import dev.paranoid.data.SimpleProfileRepository
import dev.paranoid.data.repository.ChatRepository
import dev.paranoid.data.repository.ProfileRepository
import dev.paranoid.viewmodel.ChatViewModel
import dev.paranoid.viewmodel.ProfileViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Package name mismatch is a black hole.
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)

            // _modules_ is a must
            modules(
                module {
                    single<ChatRepository> { SimpleChatRepository() }
                    single<ProfileRepository> { SimpleProfileRepository() }

                    viewModel { ChatViewModel(get()) }
                    viewModel { ProfileViewModel(get()) }
                },
            )
        }
    }
}
