package dev.paranoid.data.repository

import kotlinx.coroutines.flow.Flow

typealias ID = String

data class User(
    val id: ID = "",
    val name: String = "",
    val lastMessage: String = "...",
    val likes: List<ID> = listOf()
)

interface UserRepository {

    fun getRecommends(): Flow<List<User>>

    fun getMatches(): Flow<List<User>>

    fun getChats(): Flow<List<User>>

    fun like(id: ID): Flow<Result<String>>

}
