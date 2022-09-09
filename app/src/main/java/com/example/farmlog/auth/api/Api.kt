package com.example.farmlog.auth.api

import com.example.farmlog.auth.models.*
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

    /*@FormUrlEncoded
    @Headers("Content-Type:application/json")
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ):Call<LoginResponse>

     */
}