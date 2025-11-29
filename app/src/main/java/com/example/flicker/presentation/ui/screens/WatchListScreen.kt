package com.example.flicker.presentation.ui.screens

import MovieCard2
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flicker.domain.model.SessionManager
import com.example.flicker.presentation.viewmodel.watchlist.WatchlistViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    navController: NavController,
    sessionManager: SessionManager = koinInject(),
    watchlistViewModel: WatchlistViewModel = koinViewModel()
) {
    val currentUser by sessionManager.currentUser.collectAsState()
    val watchlistMovies by watchlistViewModel.watchlistMovies.collectAsState()

    if (currentUser == null) {
        // ... (La pantalla de "inicia sesión" no cambia)
        return
    }

    if (watchlistMovies.isEmpty()) {
        // Muestra un mensaje si la lista está vacía
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Your Watchlist is empty", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Add movies to watch them here", color = Color.Gray)
        }
    } else {
        // Muestra la cuadrícula de películas
        Column {
            Text("Watchlist".uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0D47A1))
                    .padding(horizontal = 0.dp)
            )

            LazyColumn (verticalArrangement = Arrangement.spacedBy(0.dp)){items(watchlistMovies) { movie ->
                MovieCard2(movie = movie, navController = navController)
            } }
        }
    }
}
