package com.example.flicker.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.flicker.domain.model.SessionManager
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.viewmodel.profile.ProfileViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

val availableIcons = mapOf(
    "Face" to Icons.Filled.Anchor,
    "AccountCircle" to Icons.Default.AccountCircle,
    "Star" to Icons.Default.Star,
    "RocketLaunch" to Icons.Default.RocketLaunch,
    "Pets" to Icons.Default.Pets,
    "SentimentSatisfied" to Icons.Default.SentimentSatisfied
)

@Composable
fun ProfileScreen(
    navController: NavController,
    sessionManager: SessionManager = koinInject(),
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val user by sessionManager.currentUser.collectAsState()
    val currentUser = user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (currentUser != null) {
            Spacer(modifier = Modifier.height(40.dp))

            val currentIcon = availableIcons[currentUser.photoUrl] ?: Icons.Default.AccountCircle

            Icon(
                imageVector = currentIcon,
                contentDescription = "Avatar de perfil actual",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .padding(8.dp),
                tint = Color.Black
            )

            Text(
                text = currentUser.username.first().uppercase() + currentUser.username.substring(1),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1)
            )

            if (!currentUser.name.isNullOrBlank() && !currentUser.lastName.isNullOrBlank()) {
                Text(
                    "${currentUser.name.first().uppercase()+currentUser.name.substring(1)}" +
                    " ${currentUser.lastName.first().uppercase()+currentUser.lastName.substring(1) }",
                    fontSize = 18.sp,
                    color = Color.DarkGray
                )
            }
            Text(
                text = currentUser.email,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Text("Elige tu avatar", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(horizontal = 10.dp)
                ) {
                    items(availableIcons.entries.toList()) { (name, iconVector) ->
                        val isSelected = currentUser.photoUrl == name
                        Icon(
                            imageVector = iconVector,
                            contentDescription = "Avatar $name",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else Color.Transparent)
                                .clickable {
                                    profileViewModel.updateProfilePicture(name)
                                }
                                .border(
                                    width = 2.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                                    shape = CircleShape
                                )
                                .padding(8.dp),
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.DarkGray
                        )
                    }
                }
            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    sessionManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Cerrar sesión"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontSize = 16.sp)
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}