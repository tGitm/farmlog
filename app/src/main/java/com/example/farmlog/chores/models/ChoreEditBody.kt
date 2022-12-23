package com.example.farmlog.chores.models

data class ChoreEditBody(
    val work_land: String?,
    val work_title: String?,
    val work_description: String?,
    val accessories_used: String?,
    val date: String?
)
