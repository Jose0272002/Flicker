package com.example.flicker.domain.usecase.users

import com.example.flicker.domain.model.User
import com.example.flicker.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(identifier: String, password: String): User {
        if (identifier.isBlank() || password.isBlank()) {
            throw IllegalArgumentException("El correo y la contraseña no pueden estar vacíos.")
        }
        val user = if (android.util.Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
            userRepository.getUserByEmail(identifier)
        } else {
            userRepository.getUserByName(identifier)
        }
            ?: throw Exception("No se encontró ningún usuario con ese correo o nombre de usuario.") // Error si el usuario es nulo

        // 2. Comparamos la contraseña proporcionada con la guardada en la base de datos.
        if (user.password != password) {
            throw Exception("La contraseña es incorrecta.")
        }

        return user
    }
}
