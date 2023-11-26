package com.example.kotlinPractice.repository

import com.example.kotlinPractice.modals.JWTUser
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<JWTUser, Int> {
    fun findByEmail(email: String):JWTUser?

}