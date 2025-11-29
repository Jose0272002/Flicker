package com.example.flicker.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.viewmodel.content.movies.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen (
    movieId: String,
    moviesViewModel: MoviesViewModel = koinViewModel(),
    navController: NavController
) {
    val movies by moviesViewModel.movies.collectAsState()
    val movie = remember(movies, movieId) {
        movies.find { it.id == movieId }
    }

    if (movie == null) {
        return
    }


    Box(modifier = Modifier.fillMaxSize()) {
        // IMAGEN DE FONDO
        AsyncImage(
            model = movie.image,
            contentDescription = "Fondo de la película",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // CAPA DE DIFUMINADO
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(0xE9000000),
                            Color(0xE9000000)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // COLUMNA DE TEXTO
        Column(
            modifier = Modifier
                .fillMaxSize() // La columna ocupa todo el espacio para poder alinear su contenido
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom // Alinea el contenido de la columna abajo
        ) {
            Button(
                onClick = { navController.navigate("${Screen.Content.route}/${movie.id}") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = MaterialTheme.shapes.medium,

            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = "Reproducir")
                Text("Reproducir")
            }
            Text(movie.name.first().uppercase() + movie.name.substring(1),
                fontSize = 30.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(1.dp))
            Row {
                for (category in movie.category) {
                    Button(
                        modifier = Modifier.border(8.dp, Color.Black),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0D47A1),
                            contentColor = Color.Black
                        ),
                        onClick = { /* Acción al hacer clic */ },
                    ) {
                        Text(category,
                            color = Color.Black,
                            maxLines = 2,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }

            Spacer(Modifier.height(10.dp))
            Text("Year: "+movie.year.toString(), color = Color.LightGray)
            Spacer(modifier = Modifier.height(5.dp))



            Text("Director: "+movie.director, color = Color.LightGray)
            Spacer(modifier = Modifier.height(5.dp))
            Text("Description: "+movie.description, color = Color.LightGray, maxLines = 5, overflow = TextOverflow.Ellipsis)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Rating: " + movie.rating.toString(),
                    fontSize = 20.sp,
                    color = Color(0xFF0D47A1)
                )
                Icon(Icons.Default.Star, contentDescription = "Rating", tint = Color(0xFF0D47A1))
            }
        }
    }
}
