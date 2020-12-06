package dev.paranoid.data.api

data class Message (val from: String, val to: String, val body: String)

typealias Chats = List<Message>
