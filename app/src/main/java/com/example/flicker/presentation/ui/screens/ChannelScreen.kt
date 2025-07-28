package com.example.flicker.presentation.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.flicker.presentation.ui.components.ChannelVideoPlayerCompose
import com.example.flicker.presentation.ui.components.VideoPlayerCompose
import com.example.flicker.presentation.viewmodels.ContentViewModel
import com.example.flicker.R

@Composable
fun ChannelScreen(
    navController: NavController,
    contentViewModel: ContentViewModel = viewModel(),
    onSetContentScreenFullscreen: (Boolean) -> Unit // Callback to notify NavGraph
) {
    val isFullscreenByButton by contentViewModel.isFullscreenByButton.collectAsState()

    val context = LocalContext.current
    val activity = context.findActivity()

    val configuration = LocalConfiguration.current
    val currentOrientation = configuration.orientation

    // Determine if full screen mode should be active.
    val shouldBeFullscreen = isFullscreenByButton || currentOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

    // DisposableEffect for managing system UI visibility, screen orientation, and screen-on flag.
    DisposableEffect(shouldBeFullscreen) {
        // Notify the NavGraph about the current full-screen state
        onSetContentScreenFullscreen(shouldBeFullscreen)

        val window = activity?.window
        if (window != null) {
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)

            if (shouldBeFullscreen) {
                // Hide system bars (status bar and navigation bar)
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                // Force landscape orientation (any sensor-detected landscape, including inverted)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE

                // Keep the screen on while content is playing
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                // Show system bars
                insetsController.show(WindowInsetsCompat.Type.systemBars())
                insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT

                // Restore portrait orientation (any sensor-detected portrait, including inverted)
                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT

                // Remove the flag to keep the screen on
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }

        // Cleanup block for when the Composable leaves the composition
        onDispose {
            // Notify NavGraph that ContentScreen is no longer in full-screen (important for BottomNavigationBar)
            onSetContentScreenFullscreen(false)

            if (window != null) {
                // Restore system bars
                WindowCompat.getInsetsController(window, window.decorView)
                    .show(WindowInsetsCompat.Type.systemBars())
                // Allow system to decide orientation (unspecified is safest for leaving a screen)
                activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                // Remove the screen-on flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

    // --- REEMPLAZO DEL SCAFFOLD CON UN SIMPLE BOX ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black) // Fondo negro para toda la pantalla
        // No aplicamos paddings aquí, ya que el video siempre llenará el espacio
    ) {
        // Video Player Container
        // El video siempre ocupará el 100% de la pantalla
        ChannelVideoPlayerCompose(
            rawResourceId = R.raw.la1_main_dvr , // Reemplaza con tu recurso de video,
            modifier = Modifier.fillMaxSize(), // VideoPlayerCompose siempre llena este Box
            onFullscreenToggle = { isFullscreen ->
                contentViewModel.setFullscreenState(isFullscreen)
            }
        )
    }
}