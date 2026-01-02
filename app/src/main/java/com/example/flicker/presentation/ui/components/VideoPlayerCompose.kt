package com.example.flicker.presentation.ui.components
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.media3.ui.PlayerView
import androidx.media3.ui.AspectRatioFrameLayout
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayerCompose(
    videoUrl: String,
    videoTitle: String = "Flicker video",
    modifier: Modifier = Modifier,
    onFullscreenToggle: ((Boolean) -> Unit)? = null
) {
    val context = LocalContext.current
    val castContext = remember { CastContext.getSharedInstance(context) }
    val sessionManager = castContext.sessionManager
    // Estado para controlar la visibilidad del botón de Cast
    var controlsVisible by remember { mutableStateOf(true) }

    var isCasting by remember {
        mutableStateOf(sessionManager.currentCastSession?.isConnected == true)
    }

    // Listener para reaccionar en tiempo real cuando el usuario conecta la TV
    DisposableEffect(sessionManager) {
        val listener = object : SessionManagerListener<CastSession> {
            override fun onSessionStarted(session: CastSession, sessionId: String) { isCasting = true }
            override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) { isCasting = true }
            override fun onSessionEnded(session: CastSession, error: Int) { isCasting = false }
            override fun onSessionStarting(p0: CastSession) {}
            override fun onSessionStartFailed(p0: CastSession, p1: Int) {}
            override fun onSessionEnding(p0: CastSession) {}
            override fun onSessionResumeFailed(p0: CastSession, p1: Int) {}
            override fun onSessionSuspended(p0: CastSession, p1: Int) {}
            override fun onSessionResuming(p0: CastSession, p1: String) {}
        }
        sessionManager.addSessionManagerListener(listener, CastSession::class.java)
        onDispose {
            sessionManager.removeSessionManagerListener(listener, CastSession::class.java)
        }
    }

    // Cargar el video en la TV cuando se conecte
    LaunchedEffect(isCasting, videoUrl) {
        if (isCasting) {
            val remoteMediaClient = sessionManager.currentCastSession?.remoteMediaClient

            val movieMetadata = MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE).apply {
                putString(MediaMetadata.KEY_TITLE, videoTitle)
            }

            val mediaInfo = MediaInfo.Builder(videoUrl)
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType(MimeTypes.VIDEO_MP4)
                .setMetadata(movieMetadata)
                .build()

            remoteMediaClient?.load(mediaInfo, true)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (isCasting) {
            // Capa visual cuando el video está en la TV
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text("Reproduciendo en TV...", color = Color.White)
            }
        } else {
            // Reproductor Local
            val exoPlayer = remember(videoUrl) {
                ExoPlayer.Builder(context).build().apply {
                    setMediaItem(MediaItem.fromUri(Uri.parse(videoUrl)))
                    prepare()
                    playWhenReady = true
                }
            }

            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = true
                        // Listener de visibilidad de controles
                        setControllerVisibilityListener(PlayerView.ControllerVisibilityListener { visibility ->
                            // visibility 0 es visible, 8 es oculto
                            controlsVisible = visibility == 0
                        })
                        onFullscreenToggle?.let { setFullscreenButtonClickListener { it(it) } }
                        setShowPreviousButton(false)
                        setShowNextButton(false)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            DisposableEffect(exoPlayer) {
                onDispose { exoPlayer.release() }
            }
        }

        // BOTÓN DE CAST SOBREPUESTO (Arriba a la derecha)
        // Aparece siempre para que el usuario pueda conectar/desconectar
        AnimatedVisibility(
            visible = controlsVisible || isCasting,
            enter = expandIn(),
            exit = shrinkOut(),
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