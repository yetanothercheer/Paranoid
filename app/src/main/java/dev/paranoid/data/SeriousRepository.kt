package dev.paranoid.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import dev.paranoid.data.api.UserService
import dev.paranoid.data.db.*
import dev.paranoid.data.repository.*
import dev.paranoid.data.repository.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take

fun computeRoomID(a: String, b: String) =
    if (a.hashCode() < b.hashCode()) {
        "${a.hashCode()}.${b.hashCode()}"
    } else {
        "${b.hashCode()}.${a.hashCode()}"
    }

class SeriousRepository(val db: UserDao, val chat: ChatRoomDao, val context: Context) : ChatRepository,
    UserRepository {
    val api = UserService()
    var myself: User? = null

    init {
        val savedUser = context.getSharedPreferences("Serious", MODE_PRIVATE).getString("User", "")
        println("savedUser: $savedUser")
        if (savedUser != "") myself = Gson().fromJson(savedUser, User::class.java)
    }

    override fun getChatsWith(b: ID): Flow<List<Chat>> = flow {
        val room_id = computeRoomID(myself!!.id, b)

        chat.get(room_id).take(1).collect {
            if (it != null) {
                emit(it.messages.toList())
                println(it)
            } else {
                chat.insert(ChatRoom(room_id, arrayOf()))
                emit(listOf<Chat>())
            }
        }

        chat.update(
            ChatRoom(
                room_id,
                api.getChats(room_id).get()
            )
        )

        chat.get(room_id).collect {
            println("New Message")
            emit(it!!.messages.toList())
        }
    }

    override fun chat(to: ID, body: String) = flow {
        val room_id = computeRoomID(myself!!.id, to)
        chat.get(room_id).take(1).collect {
            it!!.messages = it.messages.plus(Chat(body, myself!!.id))
            chat.update(it!!)
            println("Update $room_id")
        }

        emit(api.chat(computeRoomID(myself!!.id, to), myself!!.id, body).get())
    }

    override fun getRecommends(): Flow<List<User>> = flow {
        db.getAllUser().take(1).collect {
            emit(it.map { User(it.id, it.name) })
        }

        if (myself == null) {
            myself = api.createUser().get()
            context.getSharedPreferences("Serious", MODE_PRIVATE).edit().putString("User", Gson().toJson(myself)).apply()
        }
        val users = api.getUsers().get()

        users.map {
            if (db.get(it.id) == null) {
                db.insert(dev.paranoid.data.db.User(id = it.id, name = it.name))
            } else {
                db.update(dev.paranoid.data.db.User(id = it.id, name = it.name))
            }
        }

        db.getAllUser().take(1).collect {
            emit(it.map { User(it.id, it.name) })
        }
    }

    override fun getMatches(): Flow<List<User>> = getRecommends()

    override fun getChats(): Flow<List<User>> = getRecommends()

    override fun like(id: ID): Flow<Result<String>> = flow {
        api.like(myself!!.id, id)
    }
}
