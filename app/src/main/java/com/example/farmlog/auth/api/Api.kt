package com.example.farmlog.auth.api

import com.example.farmlog.auth.models.LoginResponse
import com.example.farmlog.auth.models.RegistrationBody
import com.example.farmlog.auth.models.RegistrationResponse
import com.example.farmlog.auth.models.SignInBody
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

    /*@FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>

     */
}