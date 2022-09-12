package com.example.farmlog.chores.api

import com.example.farmlog.chores.models.Chore
import com.example.farmlog.chores.models.ChoresResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiChores {

    // for archive screen
    @GET("get-chores/{id}")
    fun getUserChores(@Path("id") id: String?): Call<Chore>

    // for chore preview screen
    @GET("get/{chore_id}")
    fun getSingleChore(@Path("chore_id") id: String?): Call<Chore>
}