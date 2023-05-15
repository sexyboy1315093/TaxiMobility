package com.teameverywhere.taximobility.network

import com.teameverywhere.taximobility.model.DirectionResponse
import com.teameverywhere.taximobility.model.NaviItem
import com.teameverywhere.taximobility.model.RouteResult
import com.teameverywhere.taximobility.model.SearchResponse
import com.teameverywhere.taximobility.util.Constant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface NavigationApi {
    @POST("tmap/routes")
    suspend fun getRoute(
        @Header("appKey") appKey: String = Constant.TMAP_KEY,
        @Body payload: NaviItem?,
        @Query("version") version: String = "1"
    ): Response<RouteResult>
}