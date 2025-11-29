package com.example.flicker.domain.usecase.users

import com.example.flicker.domain.model.User
import com.example.flicker.domain.repository.UserRepository

class RegisterUseCase(
    private val userRepository: UserRepository
) {
    // Cambiamos el operador para que pueda lanzar excepciones
    suspend operator fun invoke(user: User) {
        // Validar que el email no esté en blanco
        if (user.email.isBlank()) {
            throw IllegalArgumentException("El email no puede estar vacío.")
        }
        // Comprobar si ya existe un usuario con ese email
        val existsByEmail = userRepository.getUserByEmail(user.email)
        if (existsByEmail != null) {
            // Si encontramos un usuario, lanzamos un error específico
            throw Exception("El email '${user.email}' ya está registrado.")
        }
        // Comprobar si ya existe un usuario con ese nombre de usuario
        val existsByUsername = userRepository.getUserByName(user.username)
        if (existsByUsername != null) {
            throw Exception("El nombre de usuario '${user.username}' ya está en uso.")
        }
        userRepository.save(user)
    }
}
