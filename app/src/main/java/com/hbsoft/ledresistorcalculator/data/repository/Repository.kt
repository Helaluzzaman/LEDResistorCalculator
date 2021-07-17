package com.hbsoft.ledresistorcalculator.data.repository

import android.util.Log
import com.hbsoft.ledresistorcalculator.data.Led
import com.hbsoft.ledresistorcalculator.data.LedData

class Repository {
    fun getAllLedData(): List<Led>{
        Log.i("Led data ", LedData.getLedList().toString() )
        return LedData.getLedList()
    }

    fun getAllLedNames(): List<String>{
        val ledList = getAllLedData()
        val ledNames = ArrayList<String>()
        ledList.forEach {
            ledNames.add(it.name)
        }
        Log.i("Led names", ledNames.toString())
        return ledNames
    }
}
