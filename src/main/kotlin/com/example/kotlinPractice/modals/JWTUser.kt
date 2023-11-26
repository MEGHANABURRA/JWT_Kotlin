package com.example.kotlinPractice.modals

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Entity
//@Table(name = "jwt_user")
class JWTUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int = 0

    @Column
    var name = ""
    @Column(unique = true)
    var email = ""

    @Column
    var password = ""
        @JsonIgnore
        get() = field
        set(value) {
            val passwordEncoder = BCryptPasswordEncoder()
            field = passwordEncoder.encode(value)
        }

    fun comparePassword(password: String): Boolean{
        return BCryptPasswordEncoder().matches(password, this.password)
    }

}