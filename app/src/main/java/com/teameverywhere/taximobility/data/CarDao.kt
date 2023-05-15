package com.teameverywhere.taximobility.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teameverywhere.taximobility.model.Car
import java.util.concurrent.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM car_tbl")
    fun getCar(): List<Car>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)
}