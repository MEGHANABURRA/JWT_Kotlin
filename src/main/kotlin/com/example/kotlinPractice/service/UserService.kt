package com.example.kotlinPractice.service

import com.example.kotlinPractice.modals.JWTUser
import com.example.kotlinPractice.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun save(user: JWTUser): JWTUser{
        return this.userRepository.save(user);
    }

    fun findByEmail(email: String): JWTUser?{
        return userRepository.findByEmail(email);
    }

    fun getById(id: Int): JWTUser{
        return this.userRepository.getById(id);
    }
}