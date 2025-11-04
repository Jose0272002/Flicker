package com.example.flicker.domain.usecase.users

import com.example.flicker.domain.model.User
import com.example.flicker.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class   GetUsersUseCase(private val userRepository: UserRepository) {
    operator fun invoke(): Flow<List<User>> {
        return userRepository.list()
    }
}