package com.example.flicker.presentation.ui.screens

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
import androidx.navigation.NavController
import com.example.flicker.presentation.ui.components.ChannelPlayerCompose
import com.example.flicker.presentation.viewmodel.channels.ChannelsViewModel
import com.example.flicker.presentation.viewmodel.content.ContentViewModel

@Composable
fun ChannelScreen(
    channelId: String,
    channelsViewModel: ChannelsViewModel,
    navController: NavController,
    contentViewModel: ContentViewModel = viewModel(),
    onSetContentScreenFullscreen: (Boolean) -> Unit // Callback to notify NavGraph
) {
    val channels by channelsViewModel.channels.collectAsState()

    // Encontrar canal por su ID
    val channel = remember(channels, channelId) {
        channels.find { it.id == channelId }
    }
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

    if (channel == null) {
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


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        ChannelPlayerCompose(
            assetFileName = channel.link  ,
            modifier = Modifier.fillMaxSize(),
            onFullscreenToggle = { isFullscreen ->
                contentViewModel.setFullscreenState(isFullscreen)
            }
        )
    }
}