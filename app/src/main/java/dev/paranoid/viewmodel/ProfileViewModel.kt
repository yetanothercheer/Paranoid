package dev.paranoid.viewmodel

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dev.paranoid.data.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(val repo: ProfileRepository) : ViewModel() {
    val profiles = repo.loadProfiles().asLiveData()
    val hasProfile = Transformations.map(profiles) { it.isNotEmpty() }

    val myProfile = repo.getMyProfile().asLiveData()

    fun loadMore() =
        viewModelScope.launch(Dispatchers.IO) {
            repo.loadMore()
        }

}
