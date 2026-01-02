package com.example.flicker.presentation.ui.components

import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.cast.framework.media.RemoteMediaClient

@OptIn(UnstableApi::class)
@Composable
fun ChannelPlayerCompose(
    localLink: String,      // Usado para el móvil (channel.link)
    tvLinks: List<String>,  // Usado para Cast (channel.linkTV)
    channelName: String,
    modifier: Modifier = Modifier,
    onFullscreenToggle: ((Boolean) -> Unit)? = null,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit
) {
    val context = LocalContext.current
    val castContext = remember { CastContext.getSharedInstance(context) }
    val sessionManager = castContext.sessionManager

    // Estado para saber si la TV está conectada
    var isCasting by remember { mutableStateOf(sessionManager.currentCastSession?.isConnected == true) }
    var controlsVisible by remember { mutableStateOf(true) }

    // Rastrear qué link de la lista TV estamos intentando reproducir
    var currentTvLinkIndex by remember(tvLinks) { mutableStateOf(0) }
    val currentTvUrl = remember(tvLinks, currentTvLinkIndex) {
        if (tvLinks.isNotEmpty()) tvLinks[currentTvLinkIndex] else ""
    }

    // Listener para detectar conexión/desconexión de Cast
    DisposableEffect(sessionManager) {
        val listener = object : SessionManagerListener<CastSession> {
            override fun onSessionStarted(s: CastSession, id: String) { isCasting = true }
            override fun onSessionResumed(s: CastSession, was: Boolean) { isCasting = true }
            override fun onSessionEnded(s: CastSession, e: Int) { isCasting = false }
            override fun onSessionStarting(s: CastSession) {}
            override fun onSessionStartFailed(s: CastSession, e: Int) {}
            override fun onSessionEnding(s: CastSession) {}
            override fun onSessionResumeFailed(s: CastSession, e: Int) {}
            override fun onSessionSuspended(s: CastSession, e: Int) {}
            override fun onSessionResuming(s: CastSession, id: String) {}
        }
        sessionManager.addSessionManagerListener(listener, CastSession::class.java)
        onDispose {
            sessionManager.removeSessionManagerListener(listener, CastSession::class.java)
        }
    }

    // --- LÓGICA DE CAST (Usa tvLinks) ---
    LaunchedEffect(isCasting, currentTvUrl) {
        if (isCasting && currentTvUrl.isNotEmpty()) {
            val remoteMediaClient = sessionManager.currentCastSession?.remoteMediaClient

            val metadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_TV_SHOW).apply {
                putString(MediaMetadata.KEY_TITLE, channelName)
            }

            val mediaInfo = MediaInfo.Builder(currentTvUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_LIVE)
                .setContentType(MimeTypes.APPLICATION_M3U8)
                .setMetadata(metadata)
                .build()

            // Listener para errores en el Chromecast
            remoteMediaClient?.registerCallback(object :
                RemoteMediaClient.Callback() {
                 fun onMediaStatusUpdated() {
                    val status = remoteMediaClient.mediaStatus
                    // Si el reproductor del Chromecast da error, saltamos al siguiente link
                    if (status?.playerState == MediaStatus.PLAYER_STATE_IDLE &&
                        status.idleReason == MediaStatus.IDLE_REASON_ERROR) {
                        if (currentTvLinkIndex < tvLinks.size - 1) {
                            currentTvLinkIndex++
                        }
                    }
                }
            })

            remoteMediaClient?.load(mediaInfo, true)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (isCasting) {
            // UI para el teléfono mientras se ve en la TV
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text("Reproduciendo '$channelName' en TV", color = Color.White)
            }
        } else {
            // --- REPRODUCTOR LOCAL (Usa localLink) ---
            val exoPlayer = remember(localLink) {
                ExoPlayer.Builder(context).build().apply {
                    val uri = if (localLink.startsWith("http")) {
                        Uri.parse(localLink)
                    } else {
                        // Soporte para cargar desde assets si el link no es URL
                        Uri.parse("file:///android_asset/$localLink")
                    }
                    setMediaItem(MediaItem.fromUri(uri))
                    prepare()
                    playWhenReady = true
                }
            }

            val forwardingPlayer = remember(exoPlayer) {
                NavigationForwardingPlayer(
                    player = exoPlayer,
                    onNext = onNextClicked,
                    onPrevious = onPreviousClicked
                )
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        this.player = forwardingPlayer
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                        setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
                            controlsVisible = visibility == 0
                        })
                        useController = true
                        onFullscreenToggle?.let { listener ->
                            setFullscreenButtonClickListener { listener(it) }
                        }
                    }
                },
                update = { view -> view.player = forwardingPlayer }
            )

            DisposableEffect(exoPlayer) {
                onDispose { exoPlayer.release() }
            }
        }

        // Botón de Cast animado (Siempre disponible sobre el video)
        AnimatedVisibility(
            visible = controlsVisible || isCasting,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            CastButton(
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
            )
        }
    }
}
