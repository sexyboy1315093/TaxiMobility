package com.teameverywhere.taximobility.model

data class Properties(
    val departIdx: Int,
    val description: String,
    val destIdx: Int,
    val distance: Int,
    val facilityType: Int,
    val fare: Int,
    val index: Int,
    val lineIndex: Int,
    val name: String,
    val nextRoadName: String,
    val pointIndex: Int,
    val pointType: String,
    val roadType: Int,
    val taxiFare: Int,
    val time: Int,
    val totalDistance: Int,
    val totalFare: Int,
    val totalTime: Int,
    val turnType: Int
)