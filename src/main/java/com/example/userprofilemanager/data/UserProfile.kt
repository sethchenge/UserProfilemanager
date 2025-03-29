package com.example.userprofilemanager.data

data class UserProfile(
    val id: Int = 0,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val age: Int = 0,
    val gender: Gender = Gender.Other,
    val hobbies: List<Hobby> = emptyList(),
    val notificationsEnabled: Boolean = false,
    val isFavorite: Boolean = false,
    val bio: String = ""
)

enum class Gender {
    Male, Female, Other
}

enum class Hobby {
    Reading, Coding, Travelling
}