package com.example.flicker.presentation.ui.components

import android.annotation.SuppressLint
import android.webkit.WebChromeClient // Importa WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebVideoPlayer(
    videoUrl: String,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                // Permite que el contenido se reproduzca sin un gesto del usuario
                settings.mediaPlaybackRequiresUserGesture = false

                // WebViewClient para manejar la navegación dentro del WebView
                webViewClient = WebViewClient()

                // WebChromeClient para manejar eventos de UI como la pantalla completa
                webChromeClient = WebChromeClient()

                // En lugar de inyectar HTML complejo, simplemente cargamos la URL.
                // WebView intentará manejarla con el mejor reproductor disponible.
                loadUrl(videoUrl)
            }
        },
        update = { webView ->
            // Si la URL cambia, volvemos a cargarla.
            webView.loadUrl(videoUrl)
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    )
}
