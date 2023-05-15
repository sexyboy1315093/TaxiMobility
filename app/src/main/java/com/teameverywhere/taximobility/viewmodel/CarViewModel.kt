package com.teameverywhere.taximobility.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.teameverywhere.taximobility.MyApplication
import com.teameverywhere.taximobility.data.DataOrException
import com.teameverywhere.taximobility.model.Car
import com.teameverywhere.taximobility.repository.CarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(private val carRepository: CarRepository): ViewModel(){
    var job: Job? = null

    var addCar = MutableLiveData<Car>()
    val loading = MutableLiveData<Boolean>()

    fun getCar(): List<Car>{
        return carRepository.getCar()
    }

    suspend fun insertCar(car: Car){
        carRepository.insertCar(car)
        addCar.postValue(car)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
