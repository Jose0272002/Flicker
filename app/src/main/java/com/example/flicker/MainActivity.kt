package com.example.flicker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.flicker.presentation.nav.NavGraph
import com.example.flicker.ui.theme.FlickerTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlickerTheme {
                var isContentScreenFullscreen by remember { mutableStateOf(false) }
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
