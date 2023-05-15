package com.teameverywhere.taximobility.model

data class Feature(
    val geometry: Geometry,
    val properties: Properties,
    val type: String
)