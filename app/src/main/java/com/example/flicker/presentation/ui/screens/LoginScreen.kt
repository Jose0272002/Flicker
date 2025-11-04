package com.example.flicker.presentation.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.viewmodel.login.LoginUiState
import com.example.flicker.presentation.viewmodel.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    // Koin se encarga de proveer el ViewModel con sus dependencias
    loginViewModel: LoginViewModel = koinViewModel()
) {
    // 1. Recolectamos los estados desde el ViewModel
    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val loginState by loginViewModel.loginUiState.collectAsState()

    // El botón se activa si ambos campos tienen texto y la contraseña tiene al menos 6 caracteres
    val isButtonEnabled = username.isNotBlank() && password.length >= 6

    // 2. Usamos LaunchedEffect para reaccionar a los cambios de estado del login
    // Este bloque se ejecutará cada vez que 'loginState' cambie
    LaunchedEffect(key1 = loginState) {
        when (loginState) {
            is LoginUiState.Success -> {
                // Navegamos a Home y limpiamos el historial para que no se pueda volver
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            // Para Loading y Error, la UI se encarga por sí sola
            else -> Unit
        }
    }

    Scaffold { paddingValues ->
        // 3. Mostramos diferentes composables dependiendo del estado
        when (val state = loginState) {
            is LoginUiState.Loading -> {
                // Estado de carga: mostramos un spinner en el centro
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoginUiState.Error -> {
                // Estado de error: mostramos un diálogo con el mensaje del ViewModel
                AlertDialog(
                    onDismissRequest = { loginViewModel.resetState() }, // Permite cerrar el diálogo
                    title = { Text(text = "Error al iniciar sesión") },
                    text = { Text(state.message) }, // Mensaje de error específico
                    confirmButton = {
                        Button(onClick = { loginViewModel.resetState() }) {
                            Text("Aceptar")
                        }
                    }
                )
                // Mostramos el formulario de login detrás del diálogo
                LoginForm(
                    username = username,
                    password = password,
                    onUsernameChange = { loginViewModel.setUsername(it) },
                    onPasswordChange = { loginViewModel.setPassword(it) },
                    onLoginClick = { loginViewModel.loginWithEmail() },
                    isButtonEnabled = isButtonEnabled,
                    modifier = Modifier.padding(paddingValues)
                )
            }

            else -> { // Estado Idle o Success (justo antes de navegar)
                // Mostramos el formulario de login
                LoginForm(
                    username = username,
                    password = password,
                    onUsernameChange = { loginViewModel.setUsername(it) },
                    onPasswordChange = { loginViewModel.setPassword(it) },
                    onLoginClick = { loginViewModel.loginWithEmail() },
                    isButtonEnabled = isButtonEnabled,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}

/**
 * Un Composable separado para el formulario, haciendo el código más limpio.
 */
@Composable
fun LoginForm(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    isButtonEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp), // Padding a los lados
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(30.dp))

        TextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Email") }, // Actualizado a Email
            placeholder = { Text("tu@email.com") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(10.dp))

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            placeholder = { Text("Tu contraseña") },
            // Oculta el texto de la contraseña
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(24.dp))

        Button(
            onClick = onLoginClick,
            enabled = isButtonEnabled
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
