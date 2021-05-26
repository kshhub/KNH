package com.example.teamproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Diet::class, Memo::class, Profile::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun dietDao() : DietDao
    abstract fun memoDao() : MemoDao
    abstract fun profileDao() : ProfileDao

    companion object {
        private const val DATABASE_NAME = "appdb.db"

        fun getInstance(context: Context): AppDataBase {
            return Room.databaseBuilder(
                context, AppDataBase::class.java, DATABASE_NAME
            ).build()
        }
    }

}