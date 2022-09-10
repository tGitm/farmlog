package com.example.farmlog.auth.usermodels

data class LoginResponse(
    val error: Boolean,
    val user_token: String,
    val user: User
)