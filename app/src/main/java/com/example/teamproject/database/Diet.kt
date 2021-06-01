package com.example.teamproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Diet(
    @PrimaryKey @ColumnInfo(name = "date") val date: String?,
    @ColumnInfo(name = "type") val type: String?, // 아침, 점심, 저녁 구분
    @ColumnInfo(name = "diet") val diet: String? // 실제 식단 내용
)