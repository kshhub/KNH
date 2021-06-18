package com.example.teamproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Memo(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "index") val index: Long,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "content") val content: String?
)