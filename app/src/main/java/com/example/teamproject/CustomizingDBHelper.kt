package com.example.teamproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CustomizingDBHelper(val context: Context?)
    : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object{
        val DB_NAME="customizingdb.db"
        val DB_VERSION = 1
        val TABLE_NAME = "customizing"
        val COPTION = "coption"
        val CSETTING = "csetting"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val create_table = "create table if not exists $TABLE_NAME("+
                "$COPTION text, "+
                "$CSETTING text);"
        db!!.execSQL(create_table)
    }

    fun insertCustomizing(customizingData: CustomizingData):Boolean{
        val values = ContentValues()
        values.put(COPTION, customizingData.cOption)
        values.put(CSETTING, customizingData.cSetting)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME, null, values)>0
        db.close()
        return flag
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val drop_table = "drop table if exists $TABLE_NAME;"
        db!!.execSQL(drop_table)
        onCreate(db)
    }

    fun findCustomizing(option:String):String {
        val strsql = "select $CSETTING from $TABLE_NAME where $COPTION='$option';"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        var str:String=""
        cursor.moveToFirst()
        val attrcount = cursor.columnCount
        for(i in 0 until attrcount){
            str = cursor.getString(i)
        }
        cursor.close()
        db.close()
        return str
    }

    fun updateCustomizing(customizingdata:CustomizingData): Boolean {
        val coption = customizingdata.cOption
        val strsql = "select * from $TABLE_NAME where $COPTION ='$coption';"
        val db = writableDatabase
        val cursor = db.rawQuery(strsql, null)
        val flag = cursor.count!=0
        if(flag){
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(COPTION, customizingdata.cOption)
            values.put(CSETTING, customizingdata.cSetting)
            db.update(TABLE_NAME, values,"$COPTION=?", arrayOf(coption.toString()))
        }
        cursor.close()
        db.close()
        return  flag
    }

}