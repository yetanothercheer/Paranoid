package dev.paranoid.data.api

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import com.google.gson.Gson
import dev.paranoid.data.repository.Chat
import dev.paranoid.data.repository.ID
import dev.paranoid.data.repository.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <reified T : Any> Request.to() =
    awaitObjectResult<T>(object : ResponseDeserializable<T> {
        override fun deserialize(content: String) = Gson().fromJson(content, T::class.java)
    })

suspend inline fun Request.raw() =
    awaitStringResult()

class UserService {
    init {
        FuelManager.instance.basePath = "https://enigmatic-hollows-35001.herokuapp.com/"
    }

    suspend fun getUsers() = withContext(Dispatchers.IO) {
        Fuel
            .get("/user")
            /**
             * TODO: Request.to() not work, fix it.
             *
             * https://stackoverflow.com/questions/57963799/reified-generic-parameter-inside-coroutine-is-not-working
             *
             * List<User> not work, use Array<User>
             * https://stackoverflow.com/a/60401548
             */
            .awaitObjectResult<Array<User>>(object : ResponseDeserializable<Array<User>> {
                override fun deserialize(content: String) = Gson().fromJson(content, Array<User>::class.java)
        })
    }

    suspend fun createUser() = withContext(Dispatchers.IO) {
        Fuel
            .post("/user")
            .body("{\"action\": \"CREATE\"}")
            .set("Content-Type", "application/json")
            .to<User>()
    }

    suspend fun like(from: ID, to: ID) = Fuel
        .post("/user")
        .body("{\"action\": \"LIKE\", \"from\": \"$from\", \"to\":\"$to\"}")
        .set("Content-Type", "application/json")
        .raw()


    suspend fun getChats(room_id: String) =
        Fuel.get("/chat/$room_id")
            .awaitObjectResult<Array<Chat>>(object : ResponseDeserializable<Array<Chat>> {
                override fun deserialize(content: String) = Gson().fromJson(content, Array<Chat>::class.java)})

    suspend fun chat(room_id: String, from: ID, message: String) =
        Fuel.post("/chat/$room_id")
            .body("{\"message\":\"$message\", \"from\": \"$from\"}")
            .set("Content-Type", "application/json")
            .raw()

}
