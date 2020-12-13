package dev.paranoid.data.repository

import kotlinx.coroutines.flow.Flow

data class Message(var body: String, var from: String, var to: String)
data class Chat(var a: String, var b: String, var messages: List<Message>)

data class ChatCompact(var with: String, var lastMessage: String, var lastFrom: String)

interface ChatRepository {

    fun getChatList(): Flow<List<ChatCompact>>

    fun getChatWith(a: String, b: String): Chat

    fun sendMessage(from: String, to: String, body: String)

}
