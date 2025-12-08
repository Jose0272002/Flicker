package com.example.flicker.presentation.nav
import RegisterScreen
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.flicker.domain.model.SessionManager
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.ui.components.BottomNavigationBar
import com.example.flicker.presentation.ui.screens.ChannelScreen
import com.example.flicker.presentation.ui.screens.ContentScreen
import com.example.flicker.presentation.ui.screens.DetailsScreen
import com.example.flicker.presentation.ui.screens.HomeScreen
import com.example.flicker.presentation.ui.screens.LoginScreen
import com.example.flicker.presentation.ui.screens.ProfileScreen
import com.example.flicker.presentation.ui.screens.SearchScreen
import com.example.flicker.presentation.ui.screens.WatchlistScreen
import com.example.flicker.presentation.ui.screens.availableIcons
import com.example.flicker.presentation.ui.screens.findActivity
import org.koin.androidx.compose.getViewModel
import org.koin.compose.koinInject


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(
    sessionManager: SessionManager = koinInject(),
    onSetContentScreenFullscreen: (Boolean) -> Unit, // Callback para actualizar el estado global
    isContentScreenFullscreen: Boolean // Estado actual del contenido
) {
    val navController = rememberNavController()
    val user by sessionManager.currentUser.collectAsState()
    val isUserLoggedIn = user != null

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
                        )
                            },
                    actions = {
                        // Muestra el icono de perfil si el usuario ha iniciado sesión
                        if (isUserLoggedIn) {
                            val iconVector = availableIcons[user?.photoUrl] ?: Icons.Default.AccountCircle
                            Icon(
                                imageVector = iconVector,
                                contentDescription = "Avatar de perfil",
                                modifier = Modifier
                                    .background(Color.DarkGray, CircleShape)
                                    .padding(5.dp)
                                    .size(30.dp)
                                    .clip(CircleShape)
                                    .clickable { navController.navigate(Screen.Profile.route) },
                                tint = Color.White // O el color que prefieras para la TopAppBar
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Black,
                        titleContentColor = Color(0xFF0D47A1),
                    )
                )
            }
        },
        bottomBar = {
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            if (!isContentScreenFullscreen && currentRoute != Screen.Login.route) {
                BottomNavigationBar(navController)
            }
            val activity = LocalContext.current.findActivity()
            val window = activity?.window
            if (!isContentScreenFullscreen) {
               activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    ) { screenPadding ->

        var startDestination : String
        if (isUserLoggedIn) {
            startDestination = Screen.Home.route
        }else {
            startDestination = Screen.Login.route
        }

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .padding(screenPadding)
                .background(Color.Black)
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
            composable(Screen.Profile.route) {
                ProfileScreen(navController)
            }
            composable(Screen.Watchlist.route) {
                WatchlistScreen(navController)
            }
            composable(
                route = "${Screen.Details.route}/{movieId}",
                arguments = listOf(navArgument("movieId") { type = NavType.StringType })
            ) { backStackEntry ->
                val movieId = backStackEntry.arguments?.getString("movieId") ?: ""
                DetailsScreen(
                    movieId = movieId,
                    navController = navController
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
            composable(
                route = "${Screen.Channel.route}/{channelId}",
                arguments = listOf(
                    navArgument("channelId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val channelId = backStackEntry.arguments?.getString("channelId") ?: ""

                ChannelScreen (
                    channelId = channelId,
                    navController = navController,
                    channelsViewModel = getViewModel(),
                    // Pasamos 'true' directamente. Un canal siempre empieza en pantalla completa.
                    initialFullscreenState = true,
                    onSetContentScreenFullscreen = onSetContentScreenFullscreen
                )
            }
        }
    }
}
