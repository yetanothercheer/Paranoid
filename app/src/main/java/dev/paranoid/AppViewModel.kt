package dev.paranoid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import dev.paranoid.data.ProfileRepository
import dev.paranoid.data.api.ProfileService
import dev.paranoid.data.db.ProfileDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppViewModel(val repo: ProfileRepository, application: Application) :
    AndroidViewModel(application) {

//    val profiles = repo.getAllProfile()
//    val profileCount = Transformations.map(profiles) { it.size }
//
//    fun refreshProfile() = viewModelScope.launch {
//        repo.refreshProfile()
//    }
//
//    fun loadMoreProfile() = viewModelScope.launch {
//    }

}
