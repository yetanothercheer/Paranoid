package dev.paranoid.data

import dev.paranoid.data.repository.Chat
import dev.paranoid.data.repository.ChatCompact
import dev.paranoid.data.repository.ChatRepository
import dev.paranoid.data.repository.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.security.MessageDigest

class SimpleChatRepository : ChatRepository {
    override fun getChatList(): Flow<List<ChatCompact>> {
        return flowOf(
            listOf(
                ChatCompact("Alice", "Hello", "Alice"),
                ChatCompact("Bob", "...", "Bob"),
                ChatCompact("Alice2", "Hello", "Alice2"),
                ChatCompact("Bob2", "Hello", "Bob2"),
                ChatCompact("Bob3", "Hello", "Bob3"),
                ChatCompact("Alice3", "Hello", "myself"),
            )
        )
    }

    override fun getChatWith(a: String, b: String): Chat {
        val key = a.sha512() + b.sha512()
        if (!inMemoryMessages.containsKey(key)) {
            inMemoryMessages[key] = mutableListOf()
        }
        return Chat(
            a, b, inMemoryMessages[key]!!
        )
    }

    private val inMemoryMessages = mutableMapOf<Int, MutableList<Message>>()

    private fun String.sha512(): Int {
        val md = MessageDigest.getInstance("SHA-512")
        return md.digest(this.toByteArray()).fold(0) { num, it ->
            num + it
        }
    }

    override fun sendMessage(from: String, to: String, body: String) {
        val key = from.sha512() + to.sha512()

        if (!inMemoryMessages.containsKey(key)) {
            inMemoryMessages[key] = mutableListOf()
        }
        inMemoryMessages[key]!!.add(Message(body, from, to))
    }

}