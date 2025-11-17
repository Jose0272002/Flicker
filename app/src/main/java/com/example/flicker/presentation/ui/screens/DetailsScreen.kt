package com.example.flicker.presentation.ui.screens

import android.content.pm.ActivityInfo
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.flicker.presentation.ui.components.VideoPlayerCompose
import com.example.flicker.presentation.viewmodel.content.ContentViewModel
import com.example.flicker.presentation.viewmodel.movies.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen (
    movieId: String, // Recibir el ID de la película como parámetro de navegación
    moviesViewModel: MoviesViewModel = koinViewModel()
) {
    val movies by moviesViewModel.movies.collectAsState()

    // Encontrar la película por su ID
    val movie = remember(movies, movieId) {
        movies.find { it.id == movieId }
    }

    if (movie == null)
    {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text("Película no encontrada", color = Color.White)
        }
        return
    }


}