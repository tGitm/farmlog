package com.example.farmlog.auth.models

import retrofit2.http.Field

data class RegistrationBody(
    val first_name: String,
    val last_name: String,
    val email: String,
    val password: String,
    val gerkMID: String
)
