package com.example.flicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.flicker.presentation.nav.NavGraph
import com.example.flicker.ui.theme.FlickerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickerTheme {
                // Estado para controlar si la pantalla actual (ContentScreen) está en modo pantalla completa
                var isContentScreenFullscreen by remember { mutableStateOf(false) }
                // Pasa este estado y su actualizador a NavGraph
                NavGraph(
                    onSetContentScreenFullscreen = { isFullscreen ->
                        isContentScreenFullscreen = isFullscreen
                    },
                    isContentScreenFullscreen = isContentScreenFullscreen
                )
            }
        }
    }
}
