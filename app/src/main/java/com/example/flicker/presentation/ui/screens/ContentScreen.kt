package com.example.flicker.presentation.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.flicker.presentation.ui.components.VideoPlayerCompose
import com.example.flicker.presentation.viewmodel.content.ContentViewModel
import com.example.flicker.presentation.viewmodel.movies.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun ContentScreen(
    movieId: String, // Recibir el ID de la película como parámetro de navegación
    moviesViewModel: MoviesViewModel = koinViewModel(),
    contentViewModel: ContentViewModel = viewModel(),
    onSetContentScreenFullscreen: (Boolean) -> Unit
) {
    val movies by moviesViewModel.movies.collectAsState()
    
    // Encontrar la película por su ID
    val movie = remember(movies, movieId) {
        movies.find { it.id == movieId }
    }

    val isFullscreenByButton by contentViewModel.isFullscreenByButton.collectAsState()
    val context = LocalContext.current
    val activity = context.findActivity()

    val configuration = LocalConfiguration.current
    val currentOrientation = configuration.orientation
    val shouldBeFullscreen = isFullscreenByButton || currentOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    
    DisposableEffect(shouldBeFullscreen) {
        onSetContentScreenFullscreen(shouldBeFullscreen)

        val window = activity?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            if (shouldBeFullscreen) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                insetsController.show(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        onDispose {
            onSetContentScreenFullscreen(false)
            if (window != null) {
                WindowCompat.getInsetsController(window, window.decorView)
                    .show(WindowInsetsCompat.Type.systemBars())
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    // Mostrar mensaje si no se encontró la película
    if (movie == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Película no encontrada", color = Color.White)
        }
        return
    }

    // Mostrar el reproductor de video
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        VideoPlayerCompose(
            videoUrl = movie.link, // Usar la URL de la película encontrada
            modifier = Modifier.fillMaxSize(),
            onFullscreenToggle = { isFullscreen ->
                contentViewModel.setFullscreenState(isFullscreen)
            }
        )
    }
}
