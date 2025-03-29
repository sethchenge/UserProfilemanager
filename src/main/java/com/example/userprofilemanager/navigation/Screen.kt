package com.example.userprofilemanager.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object ProfileForm : Screen("profile_form?profileId={profileId}") {
        fun createRoute(profileId: Int? = null): String {
            return if (profileId != null) {
                "profile_form?profileId=$profileId"
            } else {
                "profile_form?profileId="
            }
        }
    }
    object ProfileDisplay : Screen("profile_display/{profileId}") {
        fun createRoute(profileId: Int): String = "profile_display/$profileId"
    }
}