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
// Importa el reproductor web
import com.example.flicker.presentation.ui.components.WebVideoPlayer
import com.example.flicker.presentation.viewmodel.content.ContentViewModel
import com.example.flicker.presentation.viewmodel.content.movies.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun VideoScreen(
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
    // La pantalla completa ahora solo se activa por la orientación,
    // ya que el botón está dentro del reproductor web.
    val shouldBeFullscreen = currentOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    DisposableEffect(shouldBeFullscreen) {
        onSetContentScreenFullscreen(shouldBeFullscreen)

        val window = activity?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            if (shouldBeFullscreen) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                // Mantenemos la lógica de rotación y de mantener la pantalla encendida
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
            Text("not found", color = Color.White)
        }
        return
    }

    // --- CAMBIO PRINCIPAL AQUÍ ---
    // Mostrar el reproductor de video web
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        // Usamos el nuevo WebVideoPlayer en lugar de VideoPlayerCompose
        WebVideoPlayer(
            videoUrl = "https://atres-live.atresmedia.com/828ea1bb0f5f0b65dd724bdbf4ce4719113a44a9_ES_1765664472_1765693212/hlsts/live/neox_usp/neox_usp.isml/neox_usp-audio_spa=128000-video=2700000.m3u8", // Usar la URL de la película encontrada
            modifier = Modifier.fillMaxSize()
        )
    }
}
