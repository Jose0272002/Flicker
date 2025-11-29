package com.example.flicker.presentation.ui.components

import android.content.res.AssetFileDescriptor
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
import android.util.Log // Use android.util.Log for Logcat visibility
import androidx.core.net.toUri
import androidx.media3.common.Player // Ensure this import is correct

@OptIn(UnstableApi::class)
@Composable
fun ChannelPlayerCompose(
    assetFileName: String,
    modifier: Modifier = Modifier,
    onFullscreenToggle: ((Boolean) -> Unit)? = null // Optional callback
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val assetUri = "file:///android_asset/" + assetFileName
            val mediaItem = try {
                MediaItem.fromUri(assetUri.toUri())
            } catch (e: Exception) {
                Log.e("ChannelVideoPlayer", "Error parsing raw resource URI: $e. URI was: $assetUri")
                MediaItem.EMPTY // Provide a fallback
            }

            if (mediaItem != MediaItem.EMPTY) {
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = true
                repeatMode = ExoPlayer.REPEAT_MODE_OFF // Good for live streams

                // --- ADD PLAYER LISTENER HERE FOR DEBUGGING ---
                addListener(object : Player.Listener {
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        super.onPlayerError(error)
                        Log.e("ExoPlayerError", "Player error for raw resource: ${error.message}", error)
                        // Log more details if available:
                        if (error.errorCode == Player.EVENT_PLAYER_ERROR) { // Use Player.ERROR_CODE_IO_UNSPECIFIED
                            Log.e("ExoPlayerError", "IO Error cause: ${error.cause?.message}")
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        super.onPlaybackStateChanged(playbackState)
                        val stateString = when (playbackState) {
                            Player.STATE_IDLE -> "IDLE"
                            Player.STATE_BUFFERING -> "BUFFERING"
                            Player.STATE_READY -> "READY"
                            Player.STATE_ENDED -> "ENDED"
                            else -> "UNKNOWN"
                        }
                        Log.d("ExoPlayerState", "Playback State: $stateString for raw resource")
                    }
                })
            }
        }
    }

    // This DisposableEffect handles initial player setup (prepare, playWhenReady).
    // Its onDispose is now removed as the main release is in DisposableEffect(Unit).
    DisposableEffect(exoPlayer) {
        // Ensure player starts if it's idle (e.g., if re-entering screen)
        if (exoPlayer.playbackState == Player.STATE_IDLE) { // Use Player.STATE_IDLE
            exoPlayer.prepare()
        }
        exoPlayer.playWhenReady = true
        onDispose {}
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
                controllerHideOnTouch = true
                controllerShowTimeoutMs = 5000

                onFullscreenToggle?.let { listener ->
                    setFullscreenButtonClickListener { isEnteringFullscreen ->
                        listener(isEnteringFullscreen)
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    )

    // This DisposableEffect is the primary point for player release
    DisposableEffect(Unit) {
        onDispose {
             exoPlayer.release()
        }
    }
}