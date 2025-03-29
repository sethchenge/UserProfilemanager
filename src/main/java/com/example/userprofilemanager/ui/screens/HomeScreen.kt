package com.example.userprofilemanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.userprofilemanager.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.userprofilemanager.data.UserProfile
import com.example.userprofilemanager.ui.viewmodel.UserProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: UserProfileViewModel,
    onNavigateToProfileForm: () -> Unit,
    onNavigateToProfileDisplay: (Int) -> Unit
) {
    val profiles by viewModel.allProfiles.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User profile manager",
                    color = Color.Cyan) },
                actions = {
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Replace with your actual image resource
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(40.dp) // Adjust size as needed
                            .clip(CircleShape) // Makes the image circular
                            .padding(end = 16.dp)
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home",
                        tint = Color.Cyan) },
                    label = { Text("Home",color = Color.Cyan) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent // Removes the background color
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToProfileForm,
                    icon = { Icon(Icons.Filled.Person, contentDescription = "New Profile",
                        tint = Color.Cyan) },
                    label = { Text("Add profile",color= Color.Cyan) }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToProfileForm,
                containerColor = Color(0xFF00BCD4)
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Profile",
                    tint = Color.Cyan)
            }
        }
    ) { padding ->
        //Background image
        Image(
            painter = painterResource(id = R.drawable.portflower),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 1.0f  // Adjust transparency (0.0f to 1.0f)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome all to user profile manager!",
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = Color.Cyan,
                    fontSize = MaterialTheme.typography.headlineSmall.fontSize * 1.5f // Increases text size by 50%
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(profiles) { profile ->
                    ProfileCard(
                        profile = profile,
                        viewModel = viewModel,
                        onProfileClick = { onNavigateToProfileDisplay(profile.id) },
                        onDeleteClick = { viewModel.deleteProfile(profile) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileCard(
    profile: UserProfile,
    viewModel: UserProfileViewModel,
    onProfileClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        onClick = onProfileClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = profile.name.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = profile.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = profile.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = { viewModel.toggleFavorite(profile) }
                ) {
                    Icon(
                        imageVector = if (profile.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Toggle Favorite",
                        tint = if (profile.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                    )
                }
                IconButton(
                    onClick = { showDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Profile"
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = "Warning",
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Delete Profile") },
            text = { Text("Remember this process cannot be undone ${profile.name}'s profile?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteClick()
                        showDialog = false
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}