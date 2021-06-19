package com.example.teamproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface MemoDao {
    @Query("select * from Memo")
    suspend fun getAllMemo(): List<Memo>

    @Insert
    suspend fun insert(memo: Memo)

    @Update
    suspend fun updateMemo(memo: Memo)

    @Query("delete from Memo")
    suspend fun deleteAllMemo()

    @Query("select * from Memo where date = :date")
    suspend fun getMemoByDate(date: String): List<Memo>
}