package com.example.farmlog.archive

data class ChoresModel(
    var userId: String,
    var landId: String,
    var workTitle: String,
    var workDescription: String,
    var accessoriesUsed: String,
    var img: String
) {
    companion object {

    }
}