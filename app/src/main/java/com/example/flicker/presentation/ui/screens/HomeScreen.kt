package com.example.flicker.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.example.flicker.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.background(color = Color(0xFF000000)),
        topBar = {
            TopAppBar(
                title = {
                    Text("FLICK ER  ",
                        Modifier.fillMaxWidth().wrapContentSize(Alignment.Center))
                        },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color(0xFF0D47A1),

                )
            )
        },
        content = { paddingValues ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier

                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Text(
                    text = "Home",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                onClick = { navController.navigate(Screen.Content.route) }
                ) {
                    Text("Video")
                }
                Button(
                    onClick = { navController.navigate(Screen.Channel.route) }
                ) {
                    Text("Channel")
                }
            }
        }
    )
}