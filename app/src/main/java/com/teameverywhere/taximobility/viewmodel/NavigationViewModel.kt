package com.teameverywhere.taximobility.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.model.*
import com.teameverywhere.taximobility.network.NavigationApi
import com.teameverywhere.taximobility.network.SearchApi
import com.teameverywhere.taximobility.view.adapter.DirectionStepAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(private val searchApi: SearchApi, private val navigationApi: NavigationApi): ViewModel() {

    val searchComplete = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    val searchResult = MutableLiveData<List<SearchResultEntity>>()

    val directionSteps = ArrayList<Feature>()
    val directionStepModels = MutableLiveData<ArrayList<Feature>>()
    val hour = MutableLiveData<Int>()
    val minute = MutableLiveData<Int>()
    val kilometer = MutableLiveData<Int>()
    val meter = MutableLiveData<Int>()

    //장소검색
    fun getPlace(place: String){
        loading.postValue(true)
        viewModelScope.launch {
            val response = searchApi.getSearchLocation(keyword = place)
            if(response.isSuccessful){
                val body = response.body()
                body?.let { searchResponse ->
                    setData(searchResponse.searchPoiInfo.pois)
                }

                searchComplete.postValue(true)
                loading.postValue(false)
            }else {
                searchComplete.postValue(false)
            }
        }
    }

    //경로검색
    fun getDirection(naviItem: NaviItem){
        loading.postValue(true)
        viewModelScope.launch {
            val response = navigationApi.getRoute(payload = naviItem)
            if(response.isSuccessful){
                val body = response.body()
                if(body?.features?.isNotEmpty() == true){
                    val seconds = response.body()?.features!![0].properties.totalTime
                    val hours = seconds / 3600
                    val minutes = (seconds % 3600) / 60

                    val distance = response.body()?.features!![0].properties.totalDistance
                    val kilometers = distance / 1000
                    val meters = distance % 1000

                    hour.postValue(hours)
                    minute.postValue(minutes)
                    kilometer.postValue(kilometers)
                    meter.postValue(meters)

                    for(i in 0 until response.body()?.features!!.size){
                        directionSteps.add(response.body()?.features!![i])
                    }

                    directionStepModels.postValue(directionSteps)
                }
            }
            loading.postValue(false)
        }
    }

    private fun setData(pois: Pois){
        val dataList = pois.poi.map {
            SearchResultEntity(
                name = it.name ?: "빌딩명 없음",
                fullAddress = makeMainAddress(it),
                locationLatLng = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }

        searchResult.postValue(dataList)
    }

    private fun makeMainAddress(poi: Poi): String =
        if(poi.secondNo?.trim().isNullOrEmpty()){
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?:"") + " " +
                    (poi.lowerAddrName?.trim() ?:"") + " " +
                    (poi.detailAddrName?.trim() ?:"") + " " +
                    poi.firstNo?.trim()
        }else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                    (poi.middleAddrName?.trim() ?: "") + " " +
                    (poi.lowerAddrName?.trim() ?: "") + " " +
                    (poi.detailAddrName?.trim() ?: "") + " " +
                    (poi.firstNo?.trim() ?: "") + " " +
                    poi.secondNo?.trim()
        }
}