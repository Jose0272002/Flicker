package com.example.flicker.presentation.nav

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.ui.components.BottomNavigationBar
import com.example.flicker.presentation.ui.screens.ContentScreen
// ... (tus otras importaciones de pantallas)

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.example.flicker.presentation.ui.screens.ChannelScreen
import com.example.flicker.presentation.ui.screens.HomeScreen
import com.example.flicker.presentation.ui.screens.LoginScreen
import com.example.flicker.presentation.ui.screens.RegisterScreen
import com.example.flicker.presentation.ui.screens.SearchScreen
import com.example.flicker.presentation.ui.screens.WatchlistScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(
    startDestination: String = Screen.Home.route,
    onSetContentScreenFullscreen: (Boolean) -> Unit, // Callback para actualizar el estado global
    isContentScreenFullscreen: Boolean // Estado actual para ocultar la barra
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            // La BottomNavigationBar solo se muestra si NO estamos en pantalla completa
            if (!isContentScreenFullscreen) {
                BottomNavigationBar(navController)
            }
        }
    ) { screenPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(screenPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(navController)
            }
            composable(Screen.Search.route) {
                SearchScreen(navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.Register.route) {
                RegisterScreen(navController)
            }
            composable(Screen.Watchlist.route) {
                WatchlistScreen(navController)
            }
            composable(Screen.Content.route) {
                ContentScreen(
                    navController = navController,
                    onSetContentScreenFullscreen = onSetContentScreenFullscreen // Pasa el callback
                )
            }
            composable(Screen.Channel.route) {
                ChannelScreen(
                    navController = navController,
                    onSetContentScreenFullscreen = onSetContentScreenFullscreen // Pasa el callback
                )
            }
        }
    }
}