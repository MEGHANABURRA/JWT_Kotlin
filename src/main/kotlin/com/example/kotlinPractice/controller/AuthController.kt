package com.example.kotlinPractice.controller

import com.example.kotlinPractice.dto.LoginDTO
import com.example.kotlinPractice.dto.Message
import com.example.kotlinPractice.dto.RegisterDTO
import com.example.kotlinPractice.modals.JWTUser
import com.example.kotlinPractice.service.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.interfaces.ECKey
import java.security.spec.ECGenParameterSpec
import java.util.*


@RestController
@RequestMapping("api")
class AuthController(private val userService: UserService) {
    @PostMapping("register")
    fun register(@RequestBody registerDto: RegisterDTO): ResponseEntity<JWTUser>{
        val user = JWTUser()
        user.name = registerDto.name
        user.email = registerDto.email
        user.password = registerDto.password
        return ResponseEntity.ok(this.userService.save(user))
    }

//    @PostMapping("login")
//    fun login(@RequestBody loginBody: LoginDTO): ResponseEntity<Any>{
//        val user =
//            this.userService.findByEmail(loginBody.email) ?: return ResponseEntity.badRequest().body(Message("User not found"));
//        if(!user.comparePassword(loginBody.password))
//            return ResponseEntity.badRequest().body(Message("Incorrect Password"))
//        val issuer = user.id.toString()
//        val SECRET_KEY = "secrey_key"
////        val key: Key = SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.jcaName)
//
//        val jwt = Jwts.builder().setIssuer(issuer)
//            .setExpiration(Date(Sy
//            stem.currentTimeMillis() + 1000*60*60*24))
//            .signWith(SignatureAlgorithm.ES512, "secret").compact()
//        return ResponseEntity.ok(jwt)
//    }

//    private val staticKey = "secret"

    private val staticKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512)

    @PostMapping("login")
    fun login(@RequestBody loginBody: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = this.userService.findByEmail(loginBody.email)
            ?: return ResponseEntity.badRequest().body(Message("User not found"))

        if (!user.comparePassword(loginBody.password)) {
            return ResponseEntity.badRequest().body(Message("Incorrect Password"))
        }

        val issuer = user.id.toString()

        val jwt = Jwts.builder()
            .setIssuer(issuer)
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
            .signWith(staticKey)
            .compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)
        return ResponseEntity.ok(Message("Success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        if (jwt == null) {
            return ResponseEntity.badRequest().body(Message("Unauthenticated"))
        }

        try {
            val body = Jwts.parserBuilder()
                .setSigningKey(staticKey)
                .build()
                .parseClaimsJws(jwt)
                .body

            return ResponseEntity.ok(body)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().body(Message("Invalid Token"))
        }
    }
    @PostMapping("logout")
    fun logout(response: HttpServletResponse): ResponseEntity<Any> {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }
}