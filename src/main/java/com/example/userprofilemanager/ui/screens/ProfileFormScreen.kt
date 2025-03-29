package com.example.userprofilemanager.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.userprofilemanager.data.Gender
import com.example.userprofilemanager.data.Hobby
import com.example.userprofilemanager.data.UserProfile
import com.example.userprofilemanager.ui.viewmodel.UserProfileViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.userprofilemanager.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFormScreen(
    viewModel: UserProfileViewModel,
    profileId: Int?,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.Other) }
    var hobbies by remember { mutableStateOf(setOf<Hobby>()) }
    var notificationsEnabled by remember { mutableStateOf(false) }
    var bio by remember { mutableStateOf("") }

    LaunchedEffect(profileId) {
        if (profileId != null) {
            viewModel.getProfileById(profileId)
        }
    }

    val currentProfile by viewModel.currentProfile.collectAsState()

    LaunchedEffect(currentProfile) {
        currentProfile?.let { profile ->
            name = profile.name
            email = profile.email
            phone = profile.phone
            age = profile.age.toString()
            gender = profile.gender
            hobbies = profile.hobbies.toSet()
            notificationsEnabled = profile.notificationsEnabled
            bio = profile.bio
        }
    }

    val saveProfile = {
        if (name.isNotBlank() && email.isNotBlank()) {
            val profile = UserProfile(
                id = profileId ?: 0,
                name = name,
                email = email,
                phone = phone,
                age = age.toIntOrNull() ?: 0,
                gender = gender,
                hobbies = hobbies.toList(),
                notificationsEnabled = notificationsEnabled,
                bio = bio
            )
            viewModel.saveProfile(profile)
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = if (profileId != null) "Edit Profile" else "Create Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = saveProfile) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Save",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.blackapple),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    modifier = Modifier.fillMaxWidth()
                )

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Gender", style = MaterialTheme.typography.titleSmall)
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Gender.values().forEach { genderOption ->
                            RadioButton(
                                selected = gender == genderOption,
                                onClick = { gender = genderOption }
                            )
                            Text(
                                text = genderOption.name,
                                modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                            )
                        }
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Hobbies", style = MaterialTheme.typography.titleSmall)
                    Hobby.values().forEach { hobby ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = hobbies.contains(hobby),
                                onCheckedChange = { checked ->
                                    hobbies = if (checked) {
                                        hobbies + hobby
                                    } else {
                                        hobbies - hobby
                                    }
                                }
                            )
                            Text(
                                text = hobby.name,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )
                    Text(
                        text = "Allow push notifications",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Describe yourself") },
                    modifier = Modifier
                        .fillMaxWidth(),
                    minLines = 3
                )
            }
        }
    }
}
