package dev.paranoid.data.repository

import kotlinx.coroutines.flow.Flow

data class Chat(val message: String, val from: ID)

interface ChatRepository {

    fun getChatsWith(b: String): Flow<List<Chat>>

    fun chat(to: String, body: String): Flow<String>

}
