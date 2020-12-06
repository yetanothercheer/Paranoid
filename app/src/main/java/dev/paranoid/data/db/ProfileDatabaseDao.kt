package dev.paranoid.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProfileDatabaseDao {

    @Insert
    suspend fun insert(profile: Profile)

    @Query("SELECT * from profile_table WHERE id = :key")
    suspend fun get(key: Long): Profile?

    @Query("SELECT * from profile_table WHERE id = :key")
    suspend fun getLive(key: Long): LiveData<Profile?>

    @Query("DELETE from profile_table")
    suspend fun clear()

    @Query("SELECT * from profile_table ORDER BY id DESC")
    fun getAllProfile(): LiveData<List<Profile>>
}