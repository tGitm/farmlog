package com.example.farmlog.landsmap.models

data class GeojsonResponseItem(
    val properties: Properties,
    val geometry: Geometry,
    val _id: String,
    val type: String
)