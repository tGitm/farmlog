package com.example.farmlog.landsmap.models

data class GeojsonResponseItem(
    val _id: String,
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)