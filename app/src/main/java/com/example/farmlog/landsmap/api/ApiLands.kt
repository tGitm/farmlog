package com.example.farmlog.landsmap.api

import com.example.farmlog.landsmap.models.GeojsonResponse
import com.example.farmlog.landsmap.models.GeojsonResponseItem
import com.example.farmlog.landsmap.models.Geometry
import retrofit2.Call
import retrofit2.http.*

interface ApiLands {

    @GET("get-geometry/{id}")
    fun getLandsForSpinner(@Path("id") id: String?): Call<GeojsonResponse>

    @GET("get-geometry/{id}")
    suspend fun getLandsForMap(@Path("id") id: String?): GeojsonResponse
}