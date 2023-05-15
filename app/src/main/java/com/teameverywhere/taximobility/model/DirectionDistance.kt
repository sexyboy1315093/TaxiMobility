package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DirectionDistance(
    @SerializedName("text")
    @Expose
    var text: String,

    @SerializedName("value")
    @Expose
    var value: Int
)
