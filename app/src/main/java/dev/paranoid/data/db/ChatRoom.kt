package dev.paranoid.data.db

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import dev.paranoid.data.repository.Chat
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "chatroom_table")
data class ChatRoom(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "messages")
    var messages: Array<Chat>
)

object Converters {
    @TypeConverter
    @JvmStatic
    fun fromString(content: String): Array<Chat> = Gson().fromJson(content, Array<Chat>::class.java)

    @TypeConverter
    @JvmStatic
    fun toTimestamp(messages: Array<Chat>): String = Gson().toJson(messages)
}

@Dao
interface ChatRoomDao {
    @Insert
    suspend fun insert(room: ChatRoom)

    @Update
    suspend fun update(room: ChatRoom)

    @Query("SELECT * from chatroom_table WHERE id = :key")
    fun get(key: String): Flow<ChatRoom?>

    @Query("DELETE from chatroom_table")
    suspend fun clear()
}
