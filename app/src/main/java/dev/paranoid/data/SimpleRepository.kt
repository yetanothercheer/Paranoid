package dev.paranoid.data

import android.util.Log
import dev.paranoid.data.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking

class SimpleRepository : UserRepository, ChatRepository {

    override fun getRecommends(): Flow<List<User>> = flow {
        emit(listOf(
            User("1", "Alice"),
            User("2", "Bob"),
            User("3", "Cindy"),
        ))
    }

    override fun getMatches(): Flow<List<User>> = flow {
        emit(listOf(
            User("1", "Alice"),
            User("2", "Bob"),
            User("3", "Cindy"),
        ))
    }

    override fun getChats(): Flow<List<User>> = flow {
        emit(listOf(
            User("1", "Alice"),
            User("2", "Bob"),
            User("3", "Cindy"),
        ))
    }

    override fun like(id: ID): Flow<Result<String>> {
        return flow {
            Result.success("")
        }
    }

    private val chats = mutableMapOf<String, MutableList<Chat>>(
        "Alice" to mutableListOf<Chat>(
            Chat("Hi!", "Alice")
        )
    )

    override fun getChatsWith(b: String): Flow<List<Chat>> = flow {
        while (true) {
            if (chats.containsKey(b)) {
                emit(chats[b]!!)
            }
            delay(10)
        }
    }

    override fun chat(to: String, body: String) = flow {
        if (!chats.containsKey(to)) {
            chats[to] = mutableListOf()
        }
        chats[to]!!.add(Chat(body, ""))
        emit("OK")
    }

}
