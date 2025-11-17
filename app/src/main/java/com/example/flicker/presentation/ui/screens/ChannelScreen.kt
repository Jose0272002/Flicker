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
import com.example.flicker.presentation.viewmodel.content.ContentViewModel

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

    // --- REEMPLAZO DEL SCAFFOLD CON UN SIMPLE BOX ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        // El video siempre ocupará el 100% de la pantalla
        ChannelVideoPlayerCompose(
            assetFileName ="tdp_main_dvr.m3u8" ,
            modifier = Modifier.fillMaxSize(), // VideoPlayerCompose siempre llena este Box
            onFullscreenToggle = { isFullscreen ->
                contentViewModel.setFullscreenState(isFullscreen)
            }
        )
    }
}