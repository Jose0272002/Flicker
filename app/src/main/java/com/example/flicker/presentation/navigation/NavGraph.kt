import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.ui.components.BottomNavigationBar
import com.example.flicker.presentation.ui.screens.HomeScreen
import com.example.flicker.presentation.ui.screens.LoginScreen
import com.example.flicker.presentation.ui.screens.RegisterScreen
import com.example.flicker.presentation.ui.screens.SearchScreen
import com.example.flicker.presentation.ui.screens.WatchlistScreen

// El startDestination define la pantalla que se cargará cuando se abre la aplicación
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGraph(startDestination: String = Screen.Home.route) {
    // Cargamos el navController
    val navController = rememberNavController()
    Scaffold(bottomBar = { BottomNavigationBar(navController)}){screenPadding->
    // Creamos un NavHost que arranque con la pantalla de inicio
    NavHost(navController = navController, startDestination = startDestination) {
        // Definimos que para la ruta Screen.Home.route se cargue el composable HomeScreen(navController)
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.Search.route) {
            SearchScreen(navController)
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }
        composable(Screen.Watchlist.route) {
            WatchlistScreen(navController)
        }
    }
    }
}