package com.teameverywhere.taximobility.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NaviItem(
    @SerializedName("endX")
    @Expose
    val endX: Double,

    @SerializedName("endY")
    @Expose
    val endY: Double,

    @SerializedName("startX")
    @Expose
    val startX: Double,

    @SerializedName("startY")
    @Expose
    val startY: Double
)
