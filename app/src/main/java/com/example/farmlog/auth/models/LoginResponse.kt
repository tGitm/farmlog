package com.example.farmlog.auth.models

data class LoginResponse(
    val user_token: String,
    val user: User
)
