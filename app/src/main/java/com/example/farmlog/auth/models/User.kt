package com.example.farmlog.auth.models

data class User(
    val _id: String?,
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    val password: String?,
    val address: String?,
    val post: String?,
    val postalCode: Int?,
    val gerkMID: String?,
    val date: String?,
    val __v: Int
)
