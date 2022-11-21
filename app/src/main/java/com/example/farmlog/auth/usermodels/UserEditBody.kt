package com.example.farmlog.auth.usermodels

data class UserEditBody(
    val first_name: String?,
    val last_name: String?,
    val email: String?,
    //val password: String?,
    val address: String?,
    val post: String?,
    val postal_code: String?,
    val gerkMID: String?
)