package com.example.farmlog.landsmap.models

import org.json.JSONObject
import com.google.gson.JsonObject

data class GeojsonResponse(
    val lands: List<JsonObject>
)
