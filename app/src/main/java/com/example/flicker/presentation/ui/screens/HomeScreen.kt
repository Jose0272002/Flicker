package com.example.flicker.presentation.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flicker.domain.model.Movie
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.viewmodel.movies.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    moviesViewModel: MoviesViewModel= koinViewModel()
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
    
    // Obtener categorías únicas de todas las películas
    val categories = remember(movies) { 
        movies.flatMap { it.category ?: emptyList() }
            .distinct()
            .filter { it.isNotBlank() }
    }
    
    Scaffold(
        modifier = Modifier.background(color = Color.Black),
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    categories.forEach { category ->
                        val moviesInCategory = movies.filter { movie -> 
                            movie.category?.contains(category) == true 
                        }
                        
                        if (moviesInCategory.isNotEmpty()) {
                            item {
                                Text(
                                    text = category.uppercase(),
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    items(moviesInCategory) { movie ->
                                        MovieCard(movie, navController)
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Button(
                            onClick = { navController.navigate(Screen.Channel.route) },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Channel")
                        }
                    }
                }
            }
        }
    )
}
@Composable
fun MovieCard(movie: Movie, navController: NavController) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(0.dp)
            .width(120.dp)
            .height(200.dp)
            .clickable {navController.navigate("${Screen.Content.route}/${movie.id}")}
    ) {

        AsyncImage(
            model = movie.image,  // URL de la imagen
            contentDescription = movie.name,
            modifier = Modifier
                .size(200.dp)
                .clickable {navController.navigate("${Screen.Content.route}/${movie.id}")}
        )

    }
}