package com.example.flicker.presentation.ui.screens

import ChannelCard
import MovieCard
import android.app.Activity
import android.content.ClipData
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.viewmodel.channels.ChannelsViewModel
import com.example.flicker.presentation.viewmodel.content.movies.MoviesViewModel
import kotlinx.coroutines.channels.Channel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    moviesViewModel: MoviesViewModel= koinViewModel(),
    channelsViewModel: ChannelsViewModel = koinViewModel()
) {
    var showExitDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    BackHandler(enabled = true) {
        showExitDialog = true
    }
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false }, // Oculta el diálogo si se pulsa fuera.
            title = { Text("¿Salir?") },
            text = { Text("¿Desea cerrar la aplicación?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val activity = (context as? Activity)
                        activity?.finish()
                    }
                ) {
                    Text("Salir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("Continuar")
                }
            }
        )
    }

    val movies by moviesViewModel.movies.collectAsState()
    val channels by channelsViewModel.channels.collectAsState()

    // Obtener categorías únicas de todas las películas
    val categories = remember(movies) {
        movies.flatMap { it.category ?: emptyList() }
            .distinct()
            .filter { it.isNotBlank() }
    }
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

        LazyColumn(
                verticalArrangement = Arrangement.spacedBy(0.dp),
                modifier = Modifier.padding(vertical = 0.dp)
        ) {
            item {
                Text(
                    text ="Channels".uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = 0.dp)
                        .fillParentMaxWidth()
                        .padding(horizontal = 3.dp)
                        .padding(top = 1.3.dp)
                        .background(Color(0xFF0D47A1)),
                    color = Color.Black
                )
                LazyRow {
                    items(channels) { channel ->
                        ChannelCard(channel, navController)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text ="Movies".uppercase(),
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 3.dp)
                        .padding(top = 1.3.dp)
                        .background(Color(0xFF0D47A1)),
                    color = Color.Black
                )
            }
            categories.forEach { category ->
                val moviesInCategory = movies.filter { movie ->
                    movie.category?.contains(category) == true
                }

                if (moviesInCategory.isNotEmpty()) {
                    item {
                        Text(
                            text = category.first().uppercase() + category.substring(1),
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier.padding(horizontal = 0.dp)
                                .fillParentMaxWidth()
                                .padding(horizontal = 3.dp)
                                .padding(top = 1.3.dp),
                            color = Color.LightGray
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(vertical = 0.dp)
                        ) {
                            items(moviesInCategory) { movie ->
                                MovieCard(movie, navController)
                            }
                        }
                    }
                }
            }
        }
    }
}