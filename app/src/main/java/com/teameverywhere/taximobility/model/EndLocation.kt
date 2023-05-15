package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EndLocation(
    @SerializedName("lat")
    @Expose
    var lat: Double,

    @SerializedName("lng")
    @Expose
    var lng: Double
)
