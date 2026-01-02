import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.flicker.domain.model.Channel
import com.example.flicker.domain.model.Movie
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.viewmodel.watchlist.WatchlistViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieCard(movie: Movie, navController: NavController) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(0.dp)
            .height(190.dp)
            .width(130.dp)
            .clickable { navController.navigate("${Screen.Details.route}/${movie.id}") }
    ) {

        AsyncImage(
            model = movie.image,  // URL de la imagen
            contentDescription = movie.name,
            modifier = Modifier
                .height(190.dp)
                .width(130.dp)
        )

    }
}
@Composable
fun MovieCard2(movie: Movie, navController: NavController, watchlistViewModel: WatchlistViewModel = koinViewModel()) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(0.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color(0xE9000000))
            .clickable { navController.navigate("${Screen.Details.route}/${movie.id}") }
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = movie.image,
                contentDescription = movie.name,
                alignment = Alignment.TopStart,
                modifier = Modifier
                    .height(200.dp)
                    .width(130.dp)
                    .clickable { navController.navigate("${Screen.Details.route}/${movie.id}") }
            )



            Spacer(modifier = Modifier.width(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            movie.name.first().uppercase() + movie.name.substring(1),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        val watchlistIds by watchlistViewModel.watchlistIds.collectAsState()
                        val isInWatchlist = remember(watchlistIds, movie.id) {
                            watchlistIds.contains(movie.id)
                        }
                        IconToggleButton(
                            checked = isInWatchlist,
                            modifier = Modifier.padding(0.dp)
                                .size(37.dp),
                            onCheckedChange = { watchlistViewModel.toggleWatchlist(movie.id) }
                        ) {
                            Icon(
                                imageVector = if (isInWatchlist) Icons.Filled.Delete else Icons.Rounded.Add,
                                contentDescription = "add to Watchlist",
                                tint = Color(0xFF0D47A1),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Text("Year: " + movie.year.toString())
                    Text(
                        "Category: " + movie.category.toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Clip
                    )
                    Text("Director: " + movie.director)
                    Text(
                        "Description: " + movie.description,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            "Rating: " + movie.rating.toString(),
                            fontSize = 20.sp,
                            color = Color(0xFF0D47A1)
                        )
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFF0D47A1)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ChannelCard(channel: Channel, navController: NavController) {
    Column(
        modifier = Modifier
            .width(90.dp)
            .clickable {
                navController.navigate("${Screen.Channel.route}/${channel.id}")
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = channel.image,
            contentDescription = channel.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Text(
            text = channel.name,
            color = Color.White,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}