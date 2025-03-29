package com.example.userprofilemanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.userprofilemanager.ui.viewmodel.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDisplayScreen(
    viewModel: UserProfileViewModel,
    profileId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val profile by viewModel.currentProfile.collectAsState()
    
    LaunchedEffect(profileId) {
        viewModel.getProfileById(profileId)
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(profileId) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit Profile")
                    }
                }
            )
        }
    ) { padding ->
        profile?.let { userProfile ->
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = userProfile.name.take(1).uppercase(),
                                    style = MaterialTheme.typography.headlineLarge
                                )
                            }
                        }
                        IconButton(onClick = { viewModel.toggleFavorite(userProfile) }) {
                            Icon(
                                imageVector = if (userProfile.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Toggle Favorite",
                                tint = if (userProfile.isFavorite) Color.Green else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    
                    ProfileSection(title = "Name", content = userProfile.name)
                    ProfileSection(title = "Email", content = userProfile.email)
                    ProfileSection(title = "Phone", content = userProfile.phone)
                    ProfileSection(title = "Age", content = userProfile.age.toString())
                    ProfileSection(title = "Gender", content = userProfile.gender.name)
                    
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Hobbies",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            userProfile.hobbies.forEach { hobby ->
                                AssistChip(
                                    onClick = { },
                                    label = { Text(hobby.name) }
                                )
                            }
                        }
                    }
                    
                    ProfileSection(
                        title = "Notifications",
                        content = if (userProfile.notificationsEnabled) "Enabled" else "Disabled"
                    )
                    
                    ProfileSection(title = "About Me", content = userProfile.bio)
                }
            }
        }
    }
}

@Composable
fun ProfileSection(title: String, content: String) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}