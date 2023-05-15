package com.teameverywhere.taximobility.data

import androidx.room.Database
import androidx.room.Entity
import androidx.room.RoomDatabase
import com.teameverywhere.taximobility.model.Car

//데이터베이스(테이블), 데이터, DAO

@Database(entities = [Car::class], version = 1)
abstract class CarDatabase: RoomDatabase() {
    abstract fun carDao(): CarDao
}