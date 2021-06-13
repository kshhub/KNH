package com.example.teamproject.nutrition

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.teamproject.R
import com.example.teamproject.exercise.ExerciseFragment

class RecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        init()
    }

    fun init()
    {
        Log.i("fragment", "fragment init")
        //type : diet? exercise?
        val recordType = intent.getStringExtra("type")

        //넘겨줄 날짜를 담은 번들
        val bundle = Bundle(1)
        bundle.putString("date", intent.getStringExtra("date"))

        var fragment : Fragment? = null

        if(recordType == "diet")
        {
            Log.i("type", "diet")

            fragment = NutritionFactsFragment()
        }
        else if(recordType == "exercise")
        {
            Log.i("type", "exercise")

            fragment = ExerciseFragment()
        }

        fragment!!.arguments = bundle
        Log.i("fragment", "commit!")
        supportFragmentManager.beginTransaction().replace(R.id.recordFrameLayout, fragment!!).commit()
        Log.i("fragment", "commit done!")

    }
}