package dev.paranoid

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RadialGradient
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.ui.tooling.preview.Preview
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.getOrElse
import com.github.kittinunf.result.getOrNull
import kotlinx.coroutines.*

val handler = CoroutineExceptionHandler { _, exception ->
    Log.e("Coroutine Exception", "${exception.message} on ${Thread.currentThread()}")
}

data class TinderProfile(
    var id: String,
    var name: String,
    var about: String,
    var avatars: List<String>,
    var likes: List<String>
)

class TinderViewModel : ViewModel() {

    val myProfile = liveData(handler) {
        while (true) {
            if (id != -1) {
                emit(getProfile(id))
            }
            delay(1000)
        }
    }

    val matches = liveData(handler) {
        while (true) {
            if (id != -1) {
                emit((myProfile.value?.likes ?: listOf()).filter {
                    check(it.toInt())
                }.map { getProfile(it.toInt()) })
            }
            delay(1000)
        }
    }

    val allThePeople = liveData(handler) {
        emit((1..10).map { getProfile(-1) }.filter { it.id.toInt() != id }.distinctBy { it.id })
    }

    var id: Int = -1

    init {
        viewModelScope.launch(Dispatchers.IO + handler) {
            while (true) {
                register("x")
                val r = register("x")
                if (r != null) {
                    id = r.id.toInt()
                    break
                }
            }
            Log.e("ViewModel", "ID: $id")
        }
    }

    private suspend fun register(name: String): TinderProfile? = withContext(Dispatchers.IO + handler) {
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

    private suspend fun getProfile(id: Int = -1): TinderProfile = withContext(Dispatchers.IO + handler) {
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
            kotlin.runCatching {
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
    }

    private suspend fun check(_id: Int): Boolean = withContext(Dispatchers.IO + handler) {
        Fuel.post(
            "https://paranoid.yetanothercheer.vercel.app/api",
            listOf(Pair("f", "check"), Pair("a", id), Pair("b", _id))
        ).responseObject<Boolean>().third.get()
    }


}

class Main : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm: TinderViewModel by viewModels()

        setContent { UI(vm) }
    }
}

@Composable
fun ProfileCard(profile: TinderProfile) {
    Box() {
        Card(
            Modifier.fillMaxSize().clickable(onClick = {

            }),
            elevation = 10.dp
        ) {
            if (profile.avatars.isEmpty()) {
                Canvas(Modifier.fillMaxSize(), onDraw = {
                    drawRect(
                        RadialGradient(
                            listOf(Color.Red, Color.Yellow),
                            -100f,
                            100f,
                            2000f
                        )
                    )
                })
            }
        }
        Column(Modifier.align(Alignment.BottomStart).padding(10.dp)) {
            Text(
                profile.name,
                style = TextStyle(fontSize = 100.sp, color = Color.White)
            )
            if (profile.about.isNotEmpty()) {
                Text(
                    profile.about,
                    style = TextStyle(fontSize = 40.sp, color = Color.White)
                )
            }
        }
    }
}

@Composable
fun MyButton(id: Int, onClick: () -> Unit) {
    Card(elevation = 4.dp, shape = RoundedCornerShape(50)) {
        Image(
            asset = imageResource(id),
            modifier = Modifier.preferredSize(50.dp, 50.dp)
                .clickable(onClick = onClick)
        )
    }
}


@Composable
fun UI(vm: TinderViewModel) {
    MaterialTheme(
        typography = Typography(
            body1 = TextStyle(fontSize = 24.sp)
        )
    ) {
        var state by remember { mutableStateOf(0) }

        val myProfile by vm.myProfile.observeAsState(TinderProfile("", "", "", listOf(), listOf()))
        val allThePeople by vm.allThePeople.observeAsState(listOf())
        val matches by vm.matches.observeAsState(listOf())

        Column {
            TabRow(
                modifier = Modifier.preferredHeight(60.dp).background(Color.White),
                selectedTabIndex = state
            ) {
                val images =
                    listOf(R.drawable.ic_a, R.drawable.ic_b, R.drawable.ic_c, R.drawable.ic_d)

                images.forEachIndexed { index, title ->
                    Tab(
                        modifier = Modifier.background(Color.White),
                        selected = state == index,
                        onClick = { state = index }) {
                        Image(
                            asset = vectorResource(images[index]),
                            modifier = Modifier.preferredSize(30.dp, 30.dp)
                        )
                    }
                }
            }

            var index by remember { mutableStateOf(0) }

            when (state) {
                0 -> when (allThePeople.size) {
                    0 -> Text(
                        modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                        text = "Loading..."
                    )
                    else -> Column {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth().padding(7.dp)) {
                            when (allThePeople.size) {
                                0 -> Text(
                                    modifier = Modifier.fillMaxSize()
                                        .wrapContentSize(Alignment.Center), text = "Loading..."
                                )
                                else -> ProfileCard(allThePeople[index])
                            }
                        }

                        Row(
                            Modifier.padding(15.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            MyButton(R.drawable.notlike) { index++; index %= allThePeople.size }
                            Spacer(modifier = Modifier.width(50.dp))
                            MyButton(R.drawable.like) { vm.like(allThePeople[index].id.toInt()); index++; index %= allThePeople.size }
                        }
                    }
                }


                1 -> Text(
                    modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.Center),
                    text = "Not Implemented"
                )
                2 -> Column(Modifier.padding(10.dp)) {
                    LazyColumnFor(items = matches) {
                        Column {
                            Card(Modifier.fillMaxWidth()) {
                                Text("${it.name} with ID: ${it.id}", Modifier.padding(20.dp))
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
                3 -> Box(Modifier.padding(10.dp)) {
                    ProfileCard(myProfile)
                }
            }
        }
    }
}

