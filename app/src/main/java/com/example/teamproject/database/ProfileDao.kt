package com.example.teamproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProfileDao {
    @Query("select * from Profile")
    suspend fun getAllProfile(): List<Profile>

    @Insert
    suspend fun insert(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)

    @Query("delete from Profile")
    suspend fun deleteAllProfile()
}