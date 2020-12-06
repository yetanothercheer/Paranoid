package dev.paranoid.data.api

import retrofit2.Retrofit
import retrofit2.http.Field
import retrofit2.http.POST

interface ProfileService {

    @POST
    suspend fun register(@Field("f") f: String, @Field("name") name: String)

    @POST
    suspend fun getProfile(): Profile

    @POST
    suspend fun getChats(): Chats

    @POST
    suspend fun like()

    @POST
    suspend fun talk()

    companion object {
        val client: ProfileService = Retrofit.Builder()
            .baseUrl("https://paranoid.yetanothercheer.vercel.app/api")
            .build()
            .create(ProfileService::class.java)
    }
}