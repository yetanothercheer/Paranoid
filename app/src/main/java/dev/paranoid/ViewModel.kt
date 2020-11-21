package dev.paranoid

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class Profile(
    val name: String,
    val descriptor: String,
    val images: List<String>
)

fun gen(): Profile {
    (Math.random() * 40 + 60).toChar().toString()

    val name = (1..10).map { (Math.random() * 40 + 60).toChar().toString() }.reduce { acc, d ->  acc + d }
    val desc = (1..10).map { (Math.random() * 40 + 60).toChar().toString() }.reduce { acc, d ->  acc + d }
    val imageLen = (Math.random() * 4 + 2).toInt()

    return Profile(name, desc, (1..imageLen).map { "" })
}

data class TinderProfile(var id: String, var name: String, var about: String, var avatars: List<String>, var likes: List<String>)

class MyModel: ViewModel() {
    var profile: MutableLiveData<Profile> = MutableLiveData()

    init {
        GlobalScope.launch {
            Fuel.post("https://paranoid.yetanothercheer.vercel.app/api", listOf(Pair("f", "register"))).responseObject<TinderProfile> { _, _, result ->
                if (result != null) {
                    // do something...
                }
            }
        }
        profile.value = Profile("Cheer", "Yet another cheer", listOf("1", "2", "3"))
    }

    fun like() {
        profile.value = gen()
    }

    fun notLike() {
        profile.value = gen()
    }
}