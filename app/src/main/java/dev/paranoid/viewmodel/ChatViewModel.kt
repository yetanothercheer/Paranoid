package dev.paranoid.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dev.paranoid.data.repository.Chat
import dev.paranoid.data.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatViewModel(private val repo: ChatRepository) : ViewModel() {
    var current = MutableLiveData<Chat>()

    val chatList = repo.getChatList().asLiveData()

    suspend fun send(body: String) = withContext(Dispatchers.IO) {
        repo.sendMessage("myself", "someone", body)
    }

    fun loadChatWith(name: String) {
        current.value = repo.getChatWith("myself", name)
    }
}
