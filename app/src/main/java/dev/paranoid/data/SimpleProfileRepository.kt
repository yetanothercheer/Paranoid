package dev.paranoid.data

import dev.paranoid.data.repository.Profile
import dev.paranoid.data.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class SimpleProfileRepository : ProfileRepository {
    override fun loadProfiles(): Flow<List<Profile>> {
        return flowOf(
            listOf(
                Profile("Cheer", "boom..."),
                Profile("Alice", "hello~"),
                Profile("Bob", "bye."),
            )
        )
    }

    override fun loadMore() {
    }

    override fun getMyProfile(): Flow<Profile> {
        return flowOf(Profile("Cheer", "Fake me."))
    }
}
