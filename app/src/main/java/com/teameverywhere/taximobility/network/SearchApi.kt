package com.teameverywhere.taximobility.network

import com.teameverywhere.taximobility.model.AddressInfoResponse
import com.teameverywhere.taximobility.model.SearchResponse
import com.teameverywhere.taximobility.util.Constant
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchApi {
    @GET("tmap/pois")
    suspend fun getSearchLocation(
        @Header("appKey") appKey: String = Constant.TMAP_KEY,
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("count") count: Int = 20,
        @Query("searchKeyword") keyword: String,
        @Query("areaLLCode") areaLLCode: String? = null,
        @Query("areaLMCode") areaLMCode: String? = null,
        @Query("resCoordType") resCoordType: String? = null,
        @Query("searchType") searchType: String? = null,
        @Query("multiPoint") multiPoint: String? = null,
        @Query("searchtypCd") searchtypCd: String? = null,
        @Query("radius") radius: String? = null,
        @Query("reqCoordType") reqCoordType: String? = null,
        @Query("centerLon") centerLon: String? = null,
        @Query("centerLat") centerLat: String? = null,
    ): Response<SearchResponse>

    @GET("tmap/geo/reversegeocoding")
    suspend fun getReverseGeoCode(
        @Header("appKey") appKey: String = Constant.TMAP_KEY,
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("coordType") coordType: String? = null,
        @Query("addressType") addressType: String? = null
    ): Response<AddressInfoResponse>
}