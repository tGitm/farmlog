package com.example.farmlog.landsmap.api

import com.example.farmlog.landsmap.models.GeojsonResponseItem
import retrofit2.Call
import retrofit2.http.*

interface ApiLands {

    @GET("get-geometry/{id}")
    fun getLand(@Path("id") id: String?): Call<GeojsonResponseItem>
}