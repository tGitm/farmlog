package com.example.farmlog.chores.models

data class ChoreAddBody(
    val user_id: String?,
    val land_id: String?,
    val work_title: String?,
    val work_description: String?,
    val accessories_used: String?,
    val image: String?
)
