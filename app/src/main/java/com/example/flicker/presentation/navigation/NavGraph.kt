package com.example.flicker.presentation.nav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.ui.components.BottomNavigationBar
import com.example.flicker.presentation.ui.screens.ChannelScreen
import com.example.flicker.presentation.ui.screens.ContentScreen
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
                            "FLICKER",
                            Modifier
                                .fillMaxWidth()
                                .wrapContentSize(Alignment.Center)
                                .padding(top=0.dp)
                        )
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color(0xFF0D47A1),
                    ),
                )
            }
        },
        bottomBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (!isContentScreenFullscreen && currentRoute != Screen.Login.route) {
                BottomNavigationBar(navController)
            }
        }
    ) { screenPadding ->
        // Ignoramos el padding superior para que el contenido del NavHost se dibuje
        // detrás de la barra superior, eliminando el espacio visual.
        val newPadding = PaddingValues(
            start = screenPadding.calculateStartPadding(LocalLayoutDirection.current),
            // Asigna el padding superior solo si no está en modo completo y el padding calculado es mayor que 60.dp.
            // Esto previene un valor de padding negativo
            top = if (!isContentScreenFullscreen) {
                (screenPadding.calculateTopPadding() - 60.dp).coerceAtLeast(0.dp)
            } else {
                0.dp
            },
            end = screenPadding.calculateEndPadding(LocalLayoutDirection.current),
            bottom = screenPadding.calculateBottomPadding()
        )

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(newPadding)
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
                route = "${Screen.Details.route}/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                ContentScreen(
                    movieId = movieId,
                    onSetContentScreenFullscreen = onSetContentScreenFullscreen
                )
            }
            composable(
                route = "${Screen.Content.route}/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                ContentScreen(
                    movieId = movieId,
                    onSetContentScreenFullscreen = onSetContentScreenFullscreen
                )
            }
            composable(Screen.Channel.route) {
                ChannelScreen(
                    navController = navController,
                    onSetContentScreenFullscreen = onSetContentScreenFullscreen
                )
            }
        }
    }
}
