package dev.paranoid.data.repository

import kotlinx.coroutines.flow.Flow

data class Profile(val name: String, val about: String, val avatars: List<String> = listOf())

interface ProfileRepository {

    fun loadProfiles(): Flow<List<Profile>>
    fun loadMore()

    fun getMyProfile(): Flow<Profile>

}

//    fun getProfileFromDatabase() = dao.getAllProfile();
//
//    suspend fun refreshProfile() = withContext(Dispatchers.IO) {
//        client.get()
//        saveDataToDataBase()
//    }
//
//    suspend fun saveDataToDataBase() = dao.insert()
