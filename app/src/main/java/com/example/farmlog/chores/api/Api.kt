package com.example.farmlog.auth.api

import com.example.farmlog.auth.usermodels.RegistrationBody
import com.example.farmlog.auth.usermodels.RegistrationResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiChores {

    @Headers("Content-Type:application/json")
    @POST("register")
    fun createUser(
        @Body info: RegistrationBody
    ):Call<RegistrationResponse>

}