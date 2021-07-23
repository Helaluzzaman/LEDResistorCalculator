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
        const val LOW_RESISTANCE_PROBLEM = 4


        fun getLedList(): List<Led>{
            val lst = ArrayList<Led>()
            val redLed = Led(1,
                "Red",
                Color.RED,
                2.0,
                20.0
                )
            lst.add(redLed)
            val white = Led(1,
                "White",
                Color.WHITE,
                3.6,
                30.0
            )
            lst.add(white)
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
            val green = Led(1,
                "Green",
                Color.GREEN,
                2.1,
                20.0
            )
            lst.add(green)
            val blue = Led(1,
                "Blue",
                Color.BLUE,
                3.5,
                20.0
            )
            lst.add(blue)
            val infrared = Led(1,
                "Infrared",
                Color.WHITE,
                1.7,
                50.0
            )
            lst.add(infrared)
            val ultraviolet = Led(1,
                "Ultraviolet",
                Color.WHITE,
                3.6,
                25.0
            )
            lst.add(ultraviolet)


            val custom = Led(4,
                "Custom",
                Color.YELLOW,// change this
                2.1,
                20.0
            )
            lst.add(custom)

            return lst
        }
    }
}

