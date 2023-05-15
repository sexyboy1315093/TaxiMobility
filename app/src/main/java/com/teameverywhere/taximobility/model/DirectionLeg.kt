package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DirectionLeg(
    @SerializedName("distance")
    @Expose
    var distance: DirectionDistance,

    @SerializedName("duration")
    @Expose
    var duration: DirectionDuration,

    @SerializedName("end_address")
    @Expose
    var endAddress: String,

    @SerializedName("end_location")
    @Expose
    var endLocation: EndLocation,

    @SerializedName("start_address")
    @Expose
    var startAddress: String,

    @SerializedName("start_location")
    @Expose
    var startLocation: StartLocation,

    @SerializedName("steps")
    @Expose
    var steps: List<DirectionStep>? = null
)
