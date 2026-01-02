package com.example.flicker.presentation.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flicker.presentation.navigation.Screen
import com.example.flicker.presentation.viewmodel.user.LoginUiState
import com.example.flicker.presentation.viewmodel.user.LoginViewModel
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
            title = { Text("Exit App") },
            text = { Text("Are you sure you want to exit the app?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val activity = (context as? Activity)
                        activity?.finish()
                    }
                ) {
                    Text("Exit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text("Continue")
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
            .padding(horizontal = 16.dp)
            .padding(bottom = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign In",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            color = Color.White
        )

        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("User") },
            placeholder = { Text("Username or Email") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            placeholder = { Text("Introduce your password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        Spacer(modifier = Modifier.padding(10.dp))



        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onLoginClick,
                    enabled = isButtonEnabled,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                Spacer(modifier = Modifier.padding(3.dp))
                Button(
                    onClick = registerClick,
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Register",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
            }

        Spacer(Modifier.padding(16.dp))
        Button(
            onClick = noLoginClick,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Later",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
