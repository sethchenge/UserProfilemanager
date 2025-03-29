package com.example.userprofilemanager.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserProfileRepository {
    private var nextId = 1
    private val _profiles = MutableStateFlow<List<UserProfile>>(emptyList())
    
    fun getAllProfiles(): Flow<List<UserProfile>> = _profiles.asStateFlow()
    
    suspend fun getProfileById(profileId: Int): UserProfile? = 
        _profiles.value.find { it.id == profileId }
    
    suspend fun insertProfile(profile: UserProfile) {
        val newProfile = profile.copy(id = nextId++)
        _profiles.value = _profiles.value + newProfile
    }
    
    suspend fun updateProfile(profile: UserProfile) {
        _profiles.value = _profiles.value.map { 
            if (it.id == profile.id) profile else it 
        }
    }
    
    suspend fun deleteProfile(profile: UserProfile) {
        _profiles.value = _profiles.value.filter { it.id != profile.id }
    }
}