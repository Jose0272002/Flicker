package com.example.flicker.presentation.nav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.flicker.presentation.ui.screens.ChannelScreen
import com.example.flicker.presentation.ui.screens.HomeScreen
import com.example.flicker.presentation.ui.screens.LoginScreen
import com.example.flicker.presentation.ui.screens.RegisterScreen
import com.example.flicker.presentation.ui.screens.SearchScreen
import com.example.flicker.presentation.ui.screens.WatchlistScreen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(
    startDestination: String = Screen.Login.route,
    onSetContentScreenFullscreen: (Boolean) -> Unit, // Callback para actualizar el estado global
    isContentScreenFullscreen: Boolean // Estado actual del contenido
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            if (!isContentScreenFullscreen) {
                TopAppBar(
                    title = {
                        Text(
                            "FLICK ER  ",
                            Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                        )
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color(0xFF0D47A1),

                        )
                )
            }
        },
        bottomBar = {
            // La BottomNavigationBar solo se muestra si no estamos en pantalla completa
            // que tampoco se muestre si estamos en la pantalla de login o registro
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (!isContentScreenFullscreen && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route) {
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
            composable(
                route = "${Screen.Content.route}/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                ContentScreen(
                    movieId = movieId,
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