package com.hbsoft.ledresistorcalculator.data.Calculation

import kotlin.math.roundToInt

object Calculate {
    fun convertToStandardPower(input: Double): String{
        if(input < 1){
            if(input < .25) return "1/4"
            if(input < .5) return "1/2"
            if(input < .75) return "3/4"
            else return "1"
        }else{
            val fraction = input - input.toInt()
            if(fraction < .5){
                val result: Double = input.toInt() + .5
                return result.toString()
            }else{
                return input.roundToInt().toString()
            }
        }
    }
}