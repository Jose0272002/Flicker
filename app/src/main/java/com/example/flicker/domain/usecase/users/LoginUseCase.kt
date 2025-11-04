package com.example.flicker.domain.usecase.auth

import androidx.compose.ui.semantics.password
import com.example.flicker.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Valida las credenciales del usuario contra Firestore.
     * @throws Exception si el usuario no se encuentra o la contraseña es incorrecta.
     */
    suspend operator fun invoke(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            throw IllegalArgumentException("El correo y la contraseña no pueden estar vacíos.")
        }

        // 1. Buscamos al usuario por su email en Firestore a través del repositorio.
        val user = userRepository.getUserByEmail(email)
            ?: throw Exception("No se encontró ningún usuario con ese correo electrónico.") // Error si el usuario es nulo

        // 2. Comparamos la contraseña proporcionada con la guardada en la base de datos.
        // ADVERTENCIA: ¡Esto es inseguro! En una app real, deberías comparar hashes.
        if (user.password != password) {
            throw Exception("La contraseña es incorrecta.")
        }

        // 3. Si todo es correcto, la función simplemente termina. Si algo falla, lanza una excepción.
    }
}
