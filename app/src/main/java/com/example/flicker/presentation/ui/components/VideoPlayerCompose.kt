package com.example.flicker.presentation.ui.components
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerCompose(
    videoUrl: String,
    modifier: Modifier = Modifier,
    onFullscreenToggle: ((Boolean) -> Unit)? = null // Callback opcional
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
            prepare()
            playWhenReady = true
            repeatMode = ExoPlayer.REPEAT_MODE_OFF
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                setShowNextButton(false)
                setShowPreviousButton(false)
                setShowFastForwardButton(true)
                setShowRewindButton(true)
                setFullscreenButtonState(true)
                useController = true
                controllerHideOnTouch= true
                // Conecta el botón de pantalla completa de PlayerView con el listener del callback
                onFullscreenToggle?.let { listener ->
                    setFullscreenButtonClickListener { isEnteringFullscreen ->
                        listener(isEnteringFullscreen) // Llama al callback pasado desde ContentScreen
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    )

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
}