package com.example.kotlinPractice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class KotlinPracticeApplication

fun main(args: Array<String>) {
	runApplication<KotlinPracticeApplication>(*args)
}
