package dev.paranoid

import com.github.kittinunf.result.success
import dev.paranoid.data.api.UserService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import org.junit.Assert.assertEquals
import org.junit.Test

class UserServiceUnitTest {

    @Test
    fun everythingTest() {
        runBlocking {
            UserService().apply {
                createUser()
                val user = createUser().get()
                like(user.id, "any")
                getUsers().success { println(it) }

                chat("X", "Alice", "Hi")
                getChats("X").success { println(it) }
            }
        }
    }

    @Test
    fun unsolved() {
        runCatching {
            runBlocking {
                ({ Result.success("OK") })().fold({}, {})
            }
        }
    }
}