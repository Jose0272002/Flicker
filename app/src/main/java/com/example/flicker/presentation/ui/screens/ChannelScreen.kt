package com.example.flicker.presentation.ui.screens

import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.activity.result.launch
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.ui.components.ChannelPlayerCompose
import com.example.flicker.presentation.viewmodel.channels.ChannelsViewModel
import com.example.flicker.presentation.viewmodel.content.ContentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ChannelScreen(
    channelId: String,
    channelsViewModel: ChannelsViewModel,
    navController: NavController,
    initialFullscreenState: Boolean,
    contentViewModel: ContentViewModel = viewModel(),
    onSetContentScreenFullscreen: (Boolean) -> Unit // Callback to notify NavGraph
) {
    val channels by channelsViewModel.channels.collectAsState()

    // Encontrar canal por su ID
    val channel = remember(channels, channelId) {
        channels.find { it.id == channelId }
    }
    val isFullscreenByButton by contentViewModel.isFullscreenByButton.collectAsState()
    LaunchedEffect(initialFullscreenState) {
        contentViewModel.setFullscreenState(initialFullscreenState)
    }
    val context = LocalContext.current
    val activity = context.findActivity()
    val currentRoute by navController.currentBackStackEntryAsState()
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
            val isNavigatingToAnotherChannel = currentRoute?.destination?.route?.startsWith(Screen.Channel.route) == true

            if (!isNavigatingToAnotherChannel) {
                onSetContentScreenFullscreen(false)
                if (activity?.isFinishing == false && window != null) {
                    WindowCompat.getInsetsController(window, window.decorView)
                        .show(WindowInsetsCompat.Type.systemBars())
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                }
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
            },
            onNextClicked = {
                val currentIndex = channels.indexOfFirst { it.id == channelId }
                if (currentIndex != -1 && currentIndex < channels.size - 1) {
                    val nextChannelId = channels[currentIndex + 1].id
                    val route = "${Screen.Channel.route}/$nextChannelId"
                    navController.navigate(route) {
                        // Es importante actualizar el popUpTo para que coincida con la ruta completa
                        popUpTo("${Screen.Channel.route}/{channelId}") { inclusive = true }                    }
                }
            },
            onPreviousClicked = {
                val currentIndex = channels.indexOfFirst { it.id == channelId }
                if (currentIndex > 0) {
                    val previousChannelId = channels[currentIndex - 1].id
                    val route = "${Screen.Channel.route}/$previousChannelId"
                    navController.navigate(route) {
                        popUpTo("${Screen.Channel.route}/{channelId}") { inclusive = true }                    }
                }
            }
        )
    }
}