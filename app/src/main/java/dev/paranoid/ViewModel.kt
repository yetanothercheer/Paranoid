package dev.paranoid

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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


class MyModel: ViewModel() {
    var profile: MutableLiveData<Profile> = MutableLiveData()

    init {
        profile.value = Profile("Cheer", "Yet another cheer", listOf("1", "2", "3"))
    }


    fun like() {
        profile.value = gen()
    }

    fun notLike() {
        profile.value = gen()
    }
}