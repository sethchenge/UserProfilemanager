package com.example.userprofilemanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.userprofilemanager.data.UserProfile
import com.example.userprofilemanager.data.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(private val repository: UserProfileRepository) : ViewModel() {
    val allProfiles: Flow<List<UserProfile>> = repository.getAllProfiles()
    
    private val _currentProfile = MutableStateFlow<UserProfile?>(null)
    val currentProfile: StateFlow<UserProfile?> = _currentProfile.asStateFlow()
    
    fun getProfileById(profileId: Int) {
        viewModelScope.launch {
            _currentProfile.value = repository.getProfileById(profileId)
        }
    }
    
    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            if (profile.id == 0) {
                repository.insertProfile(profile)
            } else {
                repository.updateProfile(profile)
            }
        }
    }
    
    fun deleteProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.deleteProfile(profile)
        }
    }
    
    fun toggleFavorite(profile: UserProfile) {
        viewModelScope.launch {
            val updatedProfile = profile.copy(isFavorite = !profile.isFavorite)
            repository.updateProfile(updatedProfile)
        }
    }
    
    class Factory(private val repository: UserProfileRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserProfileViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserProfileViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}