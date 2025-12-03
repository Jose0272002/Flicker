package com.example.flicker.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppRegistration
import androidx.compose.material.icons.filled.CameraRoll
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(var title: String, var icon: ImageVector, val route: String) {
    object Home : Screen("Home", Icons.Default.Home,"home_screen")
    object Search : Screen("Search", Icons.Default.Search, "search_screen")
    object Login : Screen("Login", Icons.Default.Person, "login_screen")
    object Register : Screen("Register", Icons.Default.AppRegistration, "register_screen")
    object Watchlist: Screen("Watchlist", Icons.Default.Star, "watchlist_screen")
    object Profile: Screen("Profile", Icons.Default.Person, "profile_screen")
    object Details: Screen("Details", Icons.Default.CameraRoll, "details_screen/{movieId}") {
        fun createRoute(movieId: String) = "details_screen/$movieId"
    }
    object Content : Screen("Content", Icons.Default.CameraRoll, "content_screen/{movieId}") {
        fun createRoute(movieId: String) = "content_screen/$movieId"
    }
    object Channel : Screen("Channel", Icons.Default.CameraRoll, "channel_screen"){
        fun createRoute(channelId: String) = "channel_screen/$channelId"
    }
}