package com.teameverywhere.taximobility.repository

import com.teameverywhere.taximobility.data.CarDao
import com.teameverywhere.taximobility.model.Car
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarRepository @Inject constructor(private val carDao: CarDao) {
    fun getCar(): List<Car>{
        return carDao.getCar()
    }

    suspend fun insertCar(car: Car){
        carDao.insertCar(car)
    }
}