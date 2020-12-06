package dev.paranoid.data

import android.util.Log
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.getOrElse
import dev.paranoid.handler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

var myself: String = ""

data class TinderProfile(
    var id: String,
    var name: String,
    var about: String,
    var avatars: List<String>,
    var likes: List<String>
)

data class Message(var sender: String, var msg: String)

class TinderViewModel : ViewModel() {

    val myProfile = liveData {
        while (true) {
            Log.e("VM", "profile")
            if (id != -1) {
                runCatching {
                    emit(getProfile(id))
                }
            }
            delay(10000)
        }
    }

    val matches = liveData {
        while (true) {
            Log.e("VM", "matches")
            if (id != -1) {
                runCatching {
                    emit((myProfile.value?.likes ?: listOf()).filter {
                        check(it.toInt())
                    }.map { getProfile(it.toInt()) })
                }
            }
            delay(1000)
        }
    }

    val allThePeople = liveData {
        val people = mutableListOf<TinderProfile>()
        repeat(10) {
            runCatching {
                people.add(getProfile(-1))
            }
        }
        emit(people.filter { it.id.toInt() != id }.distinctBy { it.id })
    }

    var b: String = ""

    var messages = liveData {
        while (true) {
            if (b.isNotEmpty()) {
                Log.e("CHATS", "with $b at ${Thread.currentThread()}")
                val _b = b;
                runCatching {
                    val data = getChats(b)
                    if (b == _b) emit(data)
                }
            }
            delay(10)
        }
    }

    var id: Int = -1

    init {
        viewModelScope.launch(Dispatchers.IO + handler) {
            while (true) {
                val r = register("x")
                if (r != null) {
                    id = r.id.toInt()
                    myself = r.id
                    break
                }
            }
            Log.e("ViewModel", "ID: $id")
        }
    }

    private suspend fun register(name: String): TinderProfile? =
        withContext(Dispatchers.IO + handler) {
            try {
                val (_, _, result) = Fuel.post(
                    "https://paranoid.yetanothercheer.vercel.app/api",
                    listOf(Pair("f", "register"), Pair("name", name))
                ).responseObject<TinderProfile>()
                result.get()
            } catch (e: Exception) {
                Log.e("ViewModel", "Some Exception")
                e.message?.let {
                    Log.e("ViewModel", it)
                }
                null
            }
        }

    private suspend fun getProfile(id: Int = -1): TinderProfile =
        withContext(Dispatchers.IO + handler) {
            when (id) {
                -1 -> Fuel.post(
                    "https://paranoid.yetanothercheer.vercel.app/api",
                    listOf(Pair("f", "get"))
                ).responseObject<TinderProfile>().third.get()
                else -> Fuel.post(
                    "https://paranoid.yetanothercheer.vercel.app/api",
                    listOf(Pair("f", "get"), Pair("id", id))
                ).responseObject<TinderProfile>().third.get()
            }
        }

    fun like(_id: Int) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            Log.e("Like", "$id -> $_id")
            Fuel.post(
                "https://paranoid.yetanothercheer.vercel.app/api",
                listOf(Pair("f", "like"), Pair("a", id), Pair("b", _id))
            ).response().third.getOrElse { }
            Fuel.post(
                "https://paranoid.yetanothercheer.vercel.app/api",
                listOf(Pair("f", "like"), Pair("a", _id), Pair("b", id))
            ).response().third.getOrElse { }

        }
    }

    fun talk(b: String, message: String) {
        viewModelScope.launch(Dispatchers.IO + handler) {
            Log.e("TALK", "$message $b at ${Thread.currentThread()}")
            Fuel.post(
                "https://paranoid.yetanothercheer.vercel.app/api",
                listOf(Pair("f", "talk"), Pair("a", id), Pair("b", b), Pair("message", message))
            ).response().third.getOrElse {
                Log.e("TALK", "ELSE");
            }
            Log.e("TALK", "DONE");
        }
    }

    fun getChatsWith(_b: String) {
        b = _b
    }

    fun exitChats() {
        b = ""
    }

    private suspend fun getChats(b: String) = withContext(Dispatchers.IO + handler) {
        Fuel.post(
            "https://paranoid.yetanothercheer.vercel.app/api",
            listOf(Pair("f", "getChats"), Pair("a", id), Pair("b", b.toInt()))
        ).responseObject<List<Message>>().third.get()

    }

    private suspend fun check(_id: Int): Boolean = withContext(Dispatchers.IO + handler) {
        Fuel.post(
            "https://paranoid.yetanothercheer.vercel.app/api",
            listOf(Pair("f", "check"), Pair("a", id), Pair("b", _id))
        ).responseObject<Boolean>().third.get()
    }


}