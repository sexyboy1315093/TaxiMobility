package com.teameverywhere.taximobility.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "car_tbl")
data class Car(
    @PrimaryKey
    @ColumnInfo(name = "idx")
    var idx: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "company")
    var company: String,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "size")
    var size: String,

    @ColumnInfo(name = "number")
    var number: String
)
