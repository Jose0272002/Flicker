package com.example.flicker.presentation.ui.splash
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.flicker.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.flicker.ui.theme.FlickerTheme
//Asegúrate de que esta importación sea correcta para tu tema

@SuppressLint("CustomSplashScreen")
class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlickerTheme { // Usa el tema de tu aplicación definido en Compose
                SplashScreenWithBlinkingText()
            }
        }

        // Inicia una coroutine para esperar un tiempo y luego navegar a MainActivity
        lifecycleScope.launch {
            delay(3500)
            // Navega a tu MainActivity
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish() // Cierra SplashActivity para que el usuario no pueda volver a ella con el botón "Atrás"
        }
    }
}

@Composable
fun SplashScreenWithBlinkingText() {
    // Define la animación de parpadeo
    val infiniteTransition = rememberInfiniteTransition(label = "blinkingText")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f, // Completamente transparente
        targetValue = 1f,   // Completamente opaco
        animationSpec = infiniteRepeatable( // Repetir infinitamente
            animation = tween(durationMillis = 500, easing = LinearEasing), // Duración de 0.5 segundos para cada fase
            repeatMode = RepeatMode.Reverse // Va de 0 a 1 y luego de 1 a 0
        ),
        label = "alphaAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .background(Color(0xFF000000)), // Fondo negro, como Netflix
        contentAlignment = Alignment.Center // Centra el contenido (el texto)
    ) {
        // Usa AnnotatedString para aplicar diferentes estilos a partes del texto
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFF0D47A1))) { // "Flick" en azul
                    append("FLICK")
                }
                withStyle(style = SpanStyle(color = Color(0xFF0D47A1).copy(alpha = alpha))) { // "-er" en azul con el alpha animado
                    append("ER")
                }
            },
            fontSize = 48.sp, // Tamaño de fuente grande
            fontWeight = FontWeight.Bold // Negrita
        )
    }
}