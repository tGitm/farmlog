package com.example.farmlog.auth.api

import com.example.farmlog.auth.usermodels.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @POST("register")
    fun createUser(
        @Body info: RegistrationBody
    ):Call<RegistrationResponse>

    @POST("login")
    fun loginUser(
       @Body info: SignInBody
    ):Call<LoginResponse>

    @PUT("edit/{id}")
    fun editUser(@Path("id") id: String?, @Body info: UserEditBody): Call<UserEditResponse>

    @PUT("edit/password/{id}")
    fun editPassword(@Path("id") id: String?, @Body info: UserEditPassword): Call<NewPasswordResponse>

}