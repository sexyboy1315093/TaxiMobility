package com.teameverywhere.taximobility.model

data class Geometry(
    val coordinates: List<Any>,
    val traffic: List<Int>,
    val type: String
)