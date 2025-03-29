package com.example.userprofilemanager.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.userprofilemanager.data.UserProfileRepository
import com.example.userprofilemanager.ui.screens.HomeScreen
import com.example.userprofilemanager.ui.screens.ProfileDisplayScreen
import com.example.userprofilemanager.ui.screens.ProfileFormScreen
import com.example.userprofilemanager.ui.viewmodel.UserProfileViewModel

@Composable
fun AppNavigation(
    repository: UserProfileRepository,
    navController: NavHostController = rememberNavController()
) {
    val viewModel = remember { UserProfileViewModel(repository) }
    
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToProfileForm = { navController.navigate(Screen.ProfileForm.createRoute()) },
                onNavigateToProfileDisplay = { profileId ->
                    navController.navigate(Screen.ProfileDisplay.createRoute(profileId))
                }
            )
        }
        
        composable(
            route = Screen.ProfileForm.route,
            arguments = listOf(
                navArgument("profileId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString("profileId")?.toIntOrNull()
            ProfileFormScreen(
                viewModel = viewModel,
                profileId = profileId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.ProfileDisplay.route,
            arguments = listOf(
                navArgument("profileId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getInt("profileId") ?: return@composable
            ProfileDisplayScreen(
                viewModel = viewModel,
                profileId = profileId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.ProfileForm.createRoute(id))
                }
            )
        }
    }
}