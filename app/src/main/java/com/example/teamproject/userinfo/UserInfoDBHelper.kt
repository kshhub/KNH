package com.example.teamproject.userinfo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserInfoDBHelper(val context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "userInfodb.db"
        val DB_VERSION = 1
        val TABLE_NAME = "userInfo"
        val UOPTION = "uoption"
        val USETTING = "usetting"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME(" +
                "$UOPTION text, " +
                "$USETTING text);"
        db!!.execSQL(create_table)
    }

    fun insertUserInfo(userInfoData: UserInfoData): Boolean {
        val values = ContentValues()
        values.put(UOPTION, userInfoData.uOption)
        values.put(USETTING, userInfoData.uSetting)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values) > 0
        db.close()
        return flag
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    fun findUserInfo(option: String): String {
        val strsql = "select $USETTING from $TABLE_NAME where $UOPTION='$option';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        var str: String = ""
        cursor.moveToFirst()
        val attrcount = cursor.columnCount
        for (i in 0 until attrcount) {
            str = cursor.getString(i)
        }
        cursor.close()
        db.close()
        return str
    }

    fun updateUserInfo(userInfoData: UserInfoData): Boolean {
        val uoption = userInfoData.uOption
        val strsql = "select * from $TABLE_NAME where $UOPTION ='$uoption';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count != 0
        if (flag) {
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(UOPTION, userInfoData.uOption)
            values.put(USETTING, userInfoData.uSetting)
            db.update(TABLE_NAME, values, "$UOPTION=?", arrayOf(uoption.toString()))
        }
        cursor.close()
        db.close()
        return flag
    }

}