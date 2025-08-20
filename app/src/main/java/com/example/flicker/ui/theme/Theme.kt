package com.example.flicker.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FlickerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicDarkColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    // Aplicar la ocultación de la barra de navegación
    HideSystemNavigation()
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun HideSystemNavigation() {
    val view = LocalView.current
    if (!view.isInEditMode) { // No ejecutar en modo Previsualización
        val window = (view.context as? android.app.Activity)?.window
        window?.let {
            val insetsController = WindowCompat.getInsetsController(it, view)
            LaunchedEffect(Unit) {
                // Ocultar la barra de navegación del sistema
                insetsController.hide(WindowInsetsCompat.Type.navigationBars())
                // Opcional: Ocultar también la barra de estado
                // insetsController.hide(WindowInsetsCompat.Type.statusBars())

                // Definir el comportamiento cuando el usuario desliza desde los bordes
                insetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }

            // Opcional: Asegurarse de que las barras se muestren de nuevo cuando el composable se desecha
            // Esto es importante si solo quieres ocultar las barras en una pantalla específica.
            // Si quieres ocultarlas en toda la app, esto podría no ser necesario o
            // necesitarías manejarlo de forma diferente (por ejemplo, en onStop o onDestroy de la Activity).
            DisposableEffect(Unit) {
                onDispose {
                    // Volver a mostrar la barra de navegación del sistema al salir
                    // insetsController.show(WindowInsetsCompat.Type.navigationBars())
                    // Opcional: Volver a mostrar también la barra de estado
                    // insetsController.show(WindowInsetsCompat.Type.statusBars())
                }
            }
        }
    }
}