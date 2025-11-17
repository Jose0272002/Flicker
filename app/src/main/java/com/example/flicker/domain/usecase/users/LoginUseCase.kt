package com.example.flicker.domain.usecase.users

import com.example.flicker.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    /**
     * Valida las credenciales del usuario contra Firestore.
     * @throws Exception si el usuario no se encuentra o la contraseña es incorrecta.
     */
    suspend operator fun invoke(identifier: String, password: String) {
        if (identifier.isBlank() || password.isBlank()) {
            throw IllegalArgumentException("El correo y la contraseña no pueden estar vacíos.")
        }

        // 1. Verificamos si el identificador es un email o un nombre de usuario y buscamos en Firestore.
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

    }
}
