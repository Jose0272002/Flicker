import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flicker.presentation.navigation.Screen
import com.example.proyecto.presentation.viewmodel.users.RegisterUiState
import com.example.proyecto.presentation.viewmodel.users.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    registerViewModel: RegisterViewModel = koinViewModel()
) {
    val user by registerViewModel.user.collectAsState()
    val validationErrors by registerViewModel.validationErrors.collectAsState()

    val registerState by registerViewModel.registerState.collectAsState()

    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterUiState.Success -> {
                // Navegar a la pantalla de login tras un registro exitoso
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                    launchSingleTop = true
                }
                registerViewModel.resetRegisterState() // Resetea el estado
            }
            else -> {}
        }
    }

    when (val state = registerState) {
        is RegisterUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is RegisterUiState.Error -> {
            AlertDialog(
                onDismissRequest = { registerViewModel.resetRegisterState() },
                title = { Text("Error de Registro") },
                text = { Text(state.message) },
                confirmButton = {
                    Button(onClick = { registerViewModel.resetRegisterState() }) {
                        Text("Aceptar")
                    }
                }
            )
        }
        else -> {}
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp)
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = user.username,
            onValueChange = { registerViewModel.setUsername(it) },
            label = { Text("Username") },
            isError = validationErrors.containsKey("username"),
            leadingIcon = { Icon(Icons.Outlined.AccountCircle, contentDescription = "Nombre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        validationErrors["username"]?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.Start))
        }
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = user.name ?: "", onValueChange = { registerViewModel.setName(it) }, label = { Text("First Name") }, leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Nombre") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = user.lastName ?: "", onValueChange = { registerViewModel.setLastName(it) }, label = { Text("Last Name") }, leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Email") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text), singleLine = true)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = user.email, onValueChange = { registerViewModel.setEmail(it) }, label = { Text("Email") }, isError = validationErrors.containsKey("email"), leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        validationErrors["email"]?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.Start))
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = user.phone, onValueChange = { registerViewModel.setPhone(it) }, label = { Text("Phone") }, leadingIcon = { Icon(Icons.Default.Phone, contentDescription = "Email") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone), singleLine = true)
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = user.password, onValueChange = { registerViewModel.setPassword(it) }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), isError = validationErrors.containsKey("password"), leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
        validationErrors["password"]?.let { error ->
            Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.align(Alignment.Start))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                registerViewModel.save()
            },
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Register")
        }
    }
}
