package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DirectionRoute(
    @SerializedName("legs")
    @Expose
    var legs: List<DirectionLeg>? = null,

    @SerializedName("overview_polyline")
    @Expose
    var polylineModel: DirectionPolyline,

    @SerializedName("summary")
    @Expose
    var summary: String
)
