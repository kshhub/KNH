package com.example.teamproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Profile (
        @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "index") val idx: Long?,
        @ColumnInfo(name = "age") val age: Int?,
        @ColumnInfo(name = "sex") val sex: String?,
        @ColumnInfo(name = "height") val height: Int?,
        @ColumnInfo(name = "weight") val weight: Int?,
        @ColumnInfo(name = "goal") val goal: String?
)