package com.hbsoft.ledresistorcalculator.data

import android.graphics.Color
import com.hbsoft.ledresistorcalculator.R

data class Led(
    val id:Int,
    val name: String,
    val color: Int,
    var forwardVoltage_V: Double,
    var currentMax_mA:Double
)

class LedData{
    companion object{
        const val SINGLE = "single"
        const val SERIES = "series"
        const val PARALLEL = "parallel"
        fun getLedList(): List<Led>{
            val lst = ArrayList<Led>()
            val redLed = Led(1,
                "Red",
                Color.RED,
                2.0,
                20.0
                )
            lst.add(redLed)
            val redOrange = Led(2,
                "Orange",
                R.color.ORANGE,
                2.1,
                20.0
            )
            lst.add(redOrange)

            return lst
        }
    }
}

