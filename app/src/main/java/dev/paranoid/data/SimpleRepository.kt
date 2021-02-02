package dev.paranoid.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import dev.paranoid.data.repository.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SimpleRepository(val context: Context) : UserRepository, ChatRepository {
    override fun getIdentify(): User? {
        val sp = context.getSharedPreferences("Register", MODE_PRIVATE)
        val stored = sp.getString("identity", "")
        if (stored == "") {
            return null
        } else {
            return Gson().fromJson(stored, User::class.java)
        }
    }

    override fun register(name: String) = flow {
        println("Enter")
        val sp = context.getSharedPreferences("Register", MODE_PRIVATE)
        sp.edit().putString("identity", Gson().toJson(User("xxx", name, "", listOf())))
        println("Done")
        emit(true)
    }

    val users = listOf(
        User("1", "Alice"),
        User("2", "Bob"),
        User("3", "Cindy")
    )

    override fun getRecommends(): Flow<List<User>> = flow { emit(users) }

    override fun getMatches(): Flow<List<User>> = flow { emit(users) }

    override fun getChats(): Flow<List<User>> = flow { emit(users) }

    override fun like(id: ID): Flow<Result<String>> = flow { emit(Result.success("")) }

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
