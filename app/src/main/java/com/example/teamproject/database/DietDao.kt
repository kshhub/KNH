package com.example.teamproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DietDao {
    @Query("select * from Diet")
    suspend fun getAllDiet(): List<Diet>

    @Insert
    suspend fun insert(diet: Diet)

    @Update
    suspend fun updateDiet(diet: Diet)

    @Query("delete from Diet")
    suspend fun deleteAllDiet()

    @Query("select * from Diet where date = :date")
    suspend fun getDietByDate(date: String): List<Diet>
}