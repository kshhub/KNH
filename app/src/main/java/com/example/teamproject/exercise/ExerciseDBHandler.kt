package com.example.teamproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.teamproject.exercise.Exercise
import com.example.teamproject.exercise.ExerciseRecord
import java.time.LocalDate

//운동 관련 데이터베이스
class ExerciseDBHelper(val context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        const val DB_NAME = "exercise.db"
        const val DB_VERSION = 1
        const val TABLE_NAME_EXERCISE_RECORD = "exerciseRecord"
        const val TABLE_NAME_EXERCISE = "exercise"
        //기록한 시간
        const val RECORDTIME = "recordtime"
        //운동 id
        const val EID = "eid"
        //운동 이름
        const val ENAME = "ename"
        //운동의 MET계수
        const val EMET = "emet"
        //운동 시간
        const val ETIME = "etime"
        //몸무게
        const val WEIGHT = "weight"
        //소비 칼로리
        const val TOTALKCAL = "kcal"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // 운동 정보
        val createTableE = "create table if not exists $TABLE_NAME_EXERCISE(" +
                "$EID integer primary key autoincrement, " +
                "$ENAME text, " +
                "$EMET double);"
        //사용자 운동 정보
        val createTableEr = "create table if not exists $TABLE_NAME_EXERCISE_RECORD(" +
                "$RECORDTIME text primary key, " +
                "$EID integer references $TABLE_NAME_EXERCISE($EID) on update cascade, " +
                "$ETIME integer, " +
                "$WEIGHT integer, " +
                "$TOTALKCAL integer);"

        db!!.execSQL(createTableE)
        db.execSQL(createTableEr)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableEr = "drop table if exists $TABLE_NAME_EXERCISE_RECORD;"
        val dropTableE = "drop table if exists $TABLE_NAME_EXERCISE;"
        db!!.execSQL(dropTableEr)
        db.execSQL(dropTableE)
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase?) {
        super.onConfigure(db)
        db!!.setForeignKeyConstraintsEnabled(true)
    }

    fun isExerciseTableEmpty(): Boolean {
        val strsql = "select * from $TABLE_NAME_EXERCISE;"
        val db = readableDatabase
        val cursor = db.rawQuery(strsql, null)
        val count = cursor.count

        cursor.close()
        db.close()

        if (count == 0) return true

        return false
    }

    fun insertExercise(exercise: Exercise): Boolean {
        val values = ContentValues()
        values.put(EID, exercise.eid)
        values.put(ENAME, exercise.ename)
        values.put(EMET, exercise.MET)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME_EXERCISE, null, values) > 0
        db.close()
        return flag
    }

    private fun findExercise(eid: Int): Exercise? {
        val strSql = "select * from $TABLE_NAME_EXERCISE where $EID='$eid'"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count != 0
        var result: Exercise? = null

        if (cursor.count != 0) {
            cursor.moveToFirst()

            val eid = cursor.getInt(0)
            val ename = cursor.getString(1)
            val emet = cursor.getDouble(2)

            result = Exercise(eid, ename, emet)
        }

        cursor.close()
        db.close()

        return result
    }

    fun getAllRecord() {
        val strSql = "select * from $TABLE_NAME_EXERCISE_RECORD;"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)
        if (cursor.count != 0) {
            cursor.moveToFirst()
            //레코드 추가하기
            do {
                Log.i("db", cursor.getInt(1).toString().length.toString())
                Log.i("db", cursor.getString(0).toString() + " / " + cursor.getString(1))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
    }

    fun insertRecord(record: ExerciseRecord): Boolean {
        val values = ContentValues()
        values.put(RECORDTIME, record.recordtime)
        values.put(EID, record.exercise.eid)
        values.put(ETIME, record.etime)
        values.put(WEIGHT, record.weight)
        values.put(TOTALKCAL, record.totalKcal)
        val db = writableDatabase
        val flag = db.insert(TABLE_NAME_EXERCISE_RECORD, null, values) > 0
        db.close()
        return flag
    }

    fun updateRecord(record: ExerciseRecord): Boolean {
        val recordTime = record.recordtime
        val strSql = "select * from $TABLE_NAME_EXERCISE_RECORD where $RECORDTIME='$recordTime';"
        val db = writableDatabase
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count != 0
        if (flag) {
            cursor.moveToFirst()
            val values = ContentValues()
            values.put(EID, record.exercise.eid)
            values.put(ETIME, record.etime)
            values.put(WEIGHT, record.weight)
            values.put(TOTALKCAL, record.totalKcal)
            db.update(
                TABLE_NAME_EXERCISE_RECORD,
                values,
                "$RECORDTIME=?",
                arrayOf(recordTime.toString())
            )
        }

        cursor.close()
        db.close()
        return flag
    }

    fun deleteRecord(recordTime: String): Boolean {
        val strSql = "select * from $TABLE_NAME_EXERCISE_RECORD where $RECORDTIME='$recordTime'"
        val db = writableDatabase
        val cursor = db.rawQuery(strSql, null)
        val flag = cursor.count != 0
        if (flag) {
            cursor.moveToFirst()
            db.delete(TABLE_NAME_EXERCISE_RECORD, "$RECORDTIME=?", arrayOf(recordTime))
        }

        cursor.close()
        db.close()
        return flag
    }

    fun getRecordList(date: LocalDate): ArrayList<ExerciseRecord> {
        val format = date.toString()

        val strSql =
            "select * from $TABLE_NAME_EXERCISE_RECORD where $RECORDTIME like '$format%'"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)

        var list = ArrayList<ExerciseRecord>()

        if (cursor.count != 0) {
            cursor.moveToFirst()

            //리스트에 추가하기
            do {
                val recordtime = cursor.getString(0)
                val exercise = findExercise(cursor.getInt(1))
                val etime = cursor.getInt(2)
                val weight = cursor.getInt(3)
                val totalkcal = cursor.getInt(4)

                list.add(ExerciseRecord(recordtime, exercise!!, weight, etime, totalkcal))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return list
    }

    fun getAllExercise(): ArrayList<Exercise> {
        val strSql = "select * from $TABLE_NAME_EXERCISE;"
        val db = readableDatabase
        val cursor = db.rawQuery(strSql, null)

        var list = ArrayList<Exercise>()

        if (cursor.count != 0) {
            cursor.moveToFirst()

            //리스트에 추가하기
            do {
                val eid = cursor.getInt(0)
                val ename = cursor.getString(1)
                val met = cursor.getDouble(2)

                list.add(Exercise(eid, ename, met))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()

        return list
    }
}
