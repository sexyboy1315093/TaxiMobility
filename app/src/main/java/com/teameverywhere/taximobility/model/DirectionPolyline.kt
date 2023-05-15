package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DirectionPolyline(
    @SerializedName("points")
    @Expose
    var points: String
)
