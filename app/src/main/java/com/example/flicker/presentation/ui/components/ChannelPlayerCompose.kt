package com.example.flicker.presentation.ui.components

import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

@OptIn(UnstableApi::class)
@Composable
fun ChannelPlayerCompose(
    assetFileName: String,
    modifier: Modifier = Modifier,
    onFullscreenToggle: ((Boolean) -> Unit)? = null,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    val context = LocalContext.current

    // Crea un nuevo ExoPlayer cada vez que el `assetFileName` (el canal) cambia.
    val exoPlayer = remember(assetFileName) {
        ExoPlayer.Builder(context).build().apply {
            val assetUri = "file:///android_asset/$assetFileName"
            try {
                val mediaItem = MediaItem.fromUri(assetUri.toUri())
                setMediaItem(mediaItem)
                Log.i("ChannelPlayerCompose", "MediaItem cargado para: $assetFileName")
            } catch (e: Exception) {
                Log.e("ChannelPlayerCompose", "Error creando MediaItem para: $assetUri", e)
            }
        }
    }

    // Crea nuestro interceptor (ForwardingPlayer) cada vez que el exoPlayer se recrea.
    val forwardingPlayer = remember(exoPlayer) {
        NavigationForwardingPlayer(
            player = exoPlayer,
            onNext = onNextClicked,
            onPrevious = onPreviousClicked
        )
    }

    DisposableEffect(exoPlayer) {
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        // LIMPIEZA: Cuando el efecto se va (porque el canal cambió),
        // liberamos los recursos del reproductor antiguo.
        onDispose {
            exoPlayer.release()
            Log.d("ChannelPlayerCompose", "ExoPlayer liberado para el canal anterior.")
        }
    }

    // El AndroidView que muestra el reproductor.
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            // Se ejecuta solo una vez para crear la vista
            PlayerView(ctx).apply {
                this.player = forwardingPlayer // Asigna nuestro interceptor
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                setShowNextButton(true)
                setShowPreviousButton(true)
                setShowFastForwardButton(true)
                setShowRewindButton(true)
                setFullscreenButtonState(true)
                useController = true
                controllerHideOnTouch = true
                onFullscreenToggle?.let { listener ->
                    setFullscreenButtonClickListener { isEnteringFullscreen ->
                        listener(isEnteringFullscreen)
                    }
                }
            }
        },
        update = { view ->
            // Se ejecuta cada vez que hay una recomposición para asegurar
            // que la vista siempre tenga la última instancia del reproductor.
            view.player = forwardingPlayer
        }
    )
}
