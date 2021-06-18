package com.example.teamproject.custom

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CustomDBHelper(val context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        val DB_NAME = "customizingdb.db"
        val DB_VERSION = 1
        val TABLE_NAME = "customizing"
        val COPTION = "coption"
        val CSETTING = "csetting"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "create table if not exists $TABLE_NAME(" +
                "$COPTION text, " +
                "$CSETTING text);"
        db!!.execSQL(createTable)
    }

    fun insertCustomizing(customData: CustomData): Boolean {
        val values = ContentValues()
        values.put(COPTION, customData.cOption)
        values.put(CSETTING, customData.cSetting)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values) > 0
        db.close()
        return flag
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTable = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(dropTable)
        onCreate(db)
    }

    fun findCustomizing(option: String): String {
        val strSql = "select $CSETTING from $TABLE_NAME where $COPTION='$option';"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)
        var str = ""
        cursor.moveToFirst()
        val attrCount = cursor.columnCount
        for (i in 0 until attrCount) {
            str = cursor.getString(i)
        }
        cursor.close()
        db.close()
        return str
    }

    fun updateCustomizing(customizingdata: CustomData): Boolean {
        val cOption = customizingdata.cOption
        val strSql = "select * from $TABLE_NAME where $COPTION ='$cOption';"
        val db = writableDatabase
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count != 0
        if (flag) {
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(COPTION, customizingdata.cOption)
            values.put(CSETTING, customizingdata.cSetting)
            db.update(TABLE_NAME, values, "$COPTION=?", arrayOf(cOption))
        }
        cursor.close()
        db.close()
        return flag
    }

}