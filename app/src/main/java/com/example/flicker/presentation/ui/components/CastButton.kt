package com.example.flicker.presentation.ui.components

import androidx.appcompat.view.ContextThemeWrapper
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import com.example.flicker.R
import com.google.android.gms.cast.framework.CastButtonFactory

/**
 * Un Composable wrapper para el CastButton de Android usando MediaRouteButton.
 * Esto resuelve el problema de inferencia de tipos '<T : View>' en AndroidView
 * y es la forma recomendada de integración.
 */
@Composable
fun CastButton(modifier: Modifier = Modifier) {
    // Especificamos explícitamente el tipo de Vista, que es MediaRouteButton.
    AndroidView(
        factory = { context ->
            // Envolvemos el contexto con el tema de la App para asegurar colores sólidos
            val themedContext = ContextThemeWrapper(context, R.style.Theme_Flicker)
            // 1. Crea una instancia del MediaRouteButton de AndroidX.
            val mediaRouteButton = MediaRouteButton(themedContext)
            // 2. Conecta el botón al Cast Framework de Google.
            CastButtonFactory.setUpMediaRouteButton(context, mediaRouteButton)
            // 3. Devuelve el botón configurado.
            mediaRouteButton
        },
        modifier = modifier.background(color = Color.White)
    )
}
