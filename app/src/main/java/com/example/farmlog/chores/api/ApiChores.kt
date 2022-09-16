package com.example.farmlog.chores.api

import com.example.farmlog.chores.models.Chores
import com.example.farmlog.chores.models.DeleteResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiChores {

    // for chores archive screen
    @GET("get-chores/{id}")
    fun getUserChores(@Path("id") id: String?): Call<MutableList<Chores>>

    @DELETE("delete/{id}")
    fun deleteChore(@Path("id") id: String?): Call<DeleteResponse>

    // for chore preview screen
    @GET("get/{chore_id}")
    fun getSingleChore(
        @Path("chore_id") id: String?,
        @Query("_id") choreId: String?,
        @Query("user_id") userId: String?,
    ): Call<Chores>
}