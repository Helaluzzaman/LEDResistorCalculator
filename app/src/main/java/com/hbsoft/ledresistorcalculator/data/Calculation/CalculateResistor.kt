package com.hbsoft.ledresistorcalculator.data.Calculation

import java.math.RoundingMode
import java.text.NumberFormat
import kotlin.math.roundToInt

object CalculateResistor {
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
    // any value to kilo, mega and giga range converter.
    fun addKiloMegaGigaSuffix(input: Double): String{
        val nf = NumberFormat.getInstance()
        nf.roundingMode = RoundingMode.HALF_UP
        nf.maximumFractionDigits = 2
        val kiloRange = (input/1000)
        val megaRange = (input/1000000)
        val gigaRange = (input/ 1000000000)
        when{
            gigaRange >= 1 -> {
                val result = (input / 1000000000)
                return nf.format(result) + "G"
            }
            megaRange >= 1 -> {
                val result = (input / 1000000)
                return nf.format(result) + "M"
            }
            kiloRange >= 1 -> {
                val result = (input / 1000)
                return nf.format(result) + "K"
            }
            else -> {
                return nf.format(input)
            }
        }
    }

    fun generateSuggestion(resistor: Double):String{


        return ""
    }
}