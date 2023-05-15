package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DirectionResponse(
    @SerializedName("routes")
    @Expose
    var directionRouteModels: List<DirectionRoute>? = null
)
