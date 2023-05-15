package com.teameverywhere.taximobility.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teameverywhere.taximobility.model.Weather
import com.teameverywhere.taximobility.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: WeatherRepository): ViewModel(){

    private val _weather = MutableLiveData<Weather>()
    val weather get() = _weather

    private val _isStart = MutableLiveData<Boolean>()
    val isStart get() = _isStart

    private val _isVibration = MutableLiveData<Boolean>()
    val isVibration get() = _isVibration

    init {
        getWeather()
    }

    private fun getWeather(){
        viewModelScope.launch {
            repository.getWeather().let { response ->
                if(response.isSuccessful){
                    _weather.postValue(response.body())
                }else {
                    Log.d("TAG", "getWeather Error Response ${response.message()}")
                }
            }
        }
    }

    fun sendIsStart(isStart: Boolean){
        _isStart.value = isStart
    }

    fun isVibrationSetting(isVibration: Boolean){
        _isVibration.value = isVibration
    }
}
