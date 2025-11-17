package com.example.flicker.presentation.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    loginViewModel: LoginViewModel = koinViewModel()
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


    val username by loginViewModel.username.collectAsState()
    val password by loginViewModel.password.collectAsState()
    val loginState by loginViewModel.loginUiState.collectAsState()

    val isButtonEnabled = username.isNotBlank() && password.length >= 6

    LaunchedEffect(key1 = loginState) {
        when (loginState) {
            is LoginUiState.Success -> {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
            }
            else -> Unit
        }
    }

    Scaffold { paddingValues ->
        when (val state = loginState) {
            is LoginUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoginUiState.Error -> {
                AlertDialog(
                    onDismissRequest = { loginViewModel.resetState() },
                    title = { Text(text = "Error al iniciar sesión") },
                    text = { Text(state.message) },
                    confirmButton = {
                        Button(onClick = { loginViewModel.resetState() }) {
                            Text("Aceptar")
                        }
                    }
                )
                LoginForm(
                    username = username,
                    password = password,
                    onUsernameChange = { loginViewModel.setUsername(it) },
                    onPasswordChange = { loginViewModel.setPassword(it) },
                    onLoginClick = { loginViewModel.login() },
                    isButtonEnabled = isButtonEnabled,
                    modifier = Modifier.padding(paddingValues),
                    noLoginClick = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true }},
                    registerClick = { navController.navigate(Screen.Register.route) }
                ) {
                    navController.navigate(Screen.Home.route)
                }
            }

            else -> {
                LoginForm(
                    username = username,
                    password = password,
                    onUsernameChange = { loginViewModel.setUsername(it) },
                    onPasswordChange = { loginViewModel.setPassword(it) },
                    onLoginClick = { loginViewModel.login() },
                    isButtonEnabled = isButtonEnabled,
                    modifier = Modifier.padding(paddingValues),
                    noLoginClick = { navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true }},
                    registerClick = { navController.navigate(Screen.Register.route) }
                ) {
                    navController.navigate(Screen.Home.route)
                }
            }
        }
    }
}

@Composable
fun LoginForm(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    isButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    noLoginClick: () -> Unit,
    registerClick: () -> Unit,
    function: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("User") },
            placeholder = { Text("Introduce your username or email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(10.dp))

        TextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            placeholder = { Text("Introduce your password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(10.dp))

        Column {
            Button(
                onClick = onLoginClick,
                enabled = isButtonEnabled,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            Button(
                onClick = registerClick,
                shape = MaterialTheme.shapes.extraSmall,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    text = "Register",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        Spacer(Modifier.padding(20.dp))
        Button(
            onClick = noLoginClick,
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.Blue
            )
        ) {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
