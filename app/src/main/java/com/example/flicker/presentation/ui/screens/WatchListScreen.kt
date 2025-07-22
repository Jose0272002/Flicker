package com.example.flicker.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flicker.presentation.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(navController: NavController) {
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
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Acción del FAB */ }) {
                Icon(imageVector =  Icons.Default.Search, contentDescription = "Search")
            }
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
                    text = "Watchlist Screen",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    // Al pulsar en el botón abre la ventana de Detalles con el parámetro 123
                    onClick = { navController.navigate(Screen.Home.route) }
                ) {
                    Text("Logout")
                }
            }
        }
    )
}