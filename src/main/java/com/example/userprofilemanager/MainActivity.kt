package com.example.userprofilemanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.userprofilemanager.data.UserProfileRepository
import com.example.userprofilemanager.navigation.AppNavigation
import com.example.userprofilemanager.ui.theme.UserProfileManagerTheme

class MainActivity : ComponentActivity() {
    private val repository = UserProfileRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserProfileManagerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(repository = repository)
                }
            }
        }
    }
}