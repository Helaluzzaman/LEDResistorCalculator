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

        const val SUCCESS = 1
        const val ZERO_PROBLEM = 0
        const val VOLTAGE_PROBLEM = 2
        const val LED_NUMBER_PROBLEM = 3


        fun getLedList(): List<Led>{
            val lst = ArrayList<Led>()
            val redLed = Led(1,
                "Red",
                Color.RED,
                2.0,
                20.0
                )
            lst.add(redLed)
            val orangeLed = Led(2,
                "Orange",
                R.color.ORANGE,
                2.1,
                20.0
            )
            lst.add(orangeLed)
            val yellow = Led(1,
                "Yellow",
                Color.YELLOW,
                2.1,
                20.0
            )
            lst.add(yellow)

            return lst
        }
    }
}

