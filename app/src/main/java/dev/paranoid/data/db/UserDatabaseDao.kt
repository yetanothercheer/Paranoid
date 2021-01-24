package dev.paranoid.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo(name = "name")
    val name: String
)

@Dao
interface UserDatabaseDao {

    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * from user_table WHERE id = :key")
    suspend fun get(key: String): User?

    @Query("DELETE from user_table")
    suspend fun clear()

    @Query("SELECT * from user_table ORDER BY id DESC")
    fun getAllUser(): Flow<List<User>>
}
