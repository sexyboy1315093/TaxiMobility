package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DirectionStep(
    @SerializedName("distance")
    @Expose
    var distance: DirectionDistance,

    @SerializedName("duration")
    @Expose
    var duration: DirectionDuration,

    @SerializedName("end_location")
    @Expose
    var endLocation: EndLocation,

    @SerializedName("html_instructions")
    @Expose
    var htmlInstructions: String,

    @SerializedName("polyline")
    @Expose
    var polyline: DirectionPolyline,

    @SerializedName("start_location")
    @Expose
    var startLocation: StartLocation,

    @SerializedName("travel_mode")
    @Expose
    var travelMode: String,

    @SerializedName("maneuver")
    @Expose
    var maneuver: String,
)
