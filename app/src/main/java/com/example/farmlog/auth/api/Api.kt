package com.example.farmlog.auth.api

import com.example.farmlog.auth.usermodels.*
import retrofit2.Call
import retrofit2.http.*

interface Api {

    @Headers("Content-Type:application/json")
    @POST("register")
    fun createUser(
        @Body info: RegistrationBody
    ):Call<RegistrationResponse>

    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(
       @Body info: SignInBody
    ):Call<LoginResponse>

    @Headers("Content-Type:application/json")
    @PUT("edit/{id}")
    fun editUser(@Path("id") id: String?, @Body info: UserEditBody): Call<UserEditResponse>

    @Headers("Content-Type:application/json")
    @PUT("edit/password/{id}")
    fun editPassword(@Path("id") id: String?, @Body info: PasswordEditBody): Call<NewPasswordResponse>

}