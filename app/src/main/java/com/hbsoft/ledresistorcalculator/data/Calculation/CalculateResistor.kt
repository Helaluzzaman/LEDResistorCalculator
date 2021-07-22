package com.hbsoft.ledresistorcalculator.data.Calculation

import android.util.Log
import com.hbsoft.ledresistorcalculator.data.CalculationData
import com.hbsoft.ledresistorcalculator.data.LedData
import com.hbsoft.ledresistorcalculator.data.Result
import java.lang.reflect.Array
import java.math.RoundingMode
import java.text.ChoiceFormat
import java.text.NumberFormat
import kotlin.math.roundToInt

object CalculateResistor {
    fun calculateResult(calculationData: CalculationData): Any{
//        Log.i("calculationData", calculationData.toString())

        val inputVoltage = calculationData.input_voltage
        val ledNumber = calculationData.led_number
        val forwardVoltage = calculationData.led.forwardVoltage_V
        val currentMax = calculationData.led.currentMax_mA
        if(inputVoltage.equals(0.0)  || forwardVoltage.equals(0.0) || currentMax.equals(0.0)){
            return LedData.ZERO_PROBLEM   // zero for any input value is filled with zero value
        }else{
            return when(calculationData.connection){
                LedData.SINGLE -> singleConnectionCalculation(inputVoltage, forwardVoltage, currentMax)
                LedData.SERIES -> seriesConnectionCalculation(inputVoltage, ledNumber, forwardVoltage, currentMax)
                LedData.PARALLEL -> parallelConnectionCalculation(inputVoltage, ledNumber, forwardVoltage, currentMax)
                else -> 100
            }
        }
    }

    private fun parallelConnectionCalculation(
        inputVoltage: Double,
        ledNumber: Int,
        forwardVoltage: Double,
        currentMax: Double
    ): Any {
        // this calculation for Parallel.
        val voltage = inputVoltage - forwardVoltage
        val current = currentMax* ledNumber
        // end
        if(!greaterThanOne(ledNumber)) return LedData.LED_NUMBER_PROBLEM    // led less then 2
        return calculateResistorPower(voltage, current)
    }

    private fun seriesConnectionCalculation(
        inputVoltage: Double,
        ledNumber: Int,
        forwardVoltage: Double,
        currentMax: Double
    ): Any {
        // this calculation for series
        val voltage = inputVoltage - (forwardVoltage*ledNumber)
        val current = currentMax
        //end
        if(!greaterThanOne(ledNumber)) return LedData.LED_NUMBER_PROBLEM    // led less then 2
        return calculateResistorPower(voltage, current)
    }

    private fun singleConnectionCalculation(
        inputVoltage: Double,
        forwardVoltage: Double,
        currentMax: Double
    ): Any {
        // this calculation for single
        val voltage = inputVoltage - forwardVoltage
        val current = currentMax
        //end
        return calculateResistorPower(voltage, current)
    }

    // general calculation for resistor and resistor power using voltage across and current through.
    private fun calculateResistorPower(voltage: Double, current: Double): Any {
        if(greaterThanZero(voltage)){
            val resistorOhm = (voltage/current)*1000
            val powerWatt = voltage* current/1000
            if(resistorOhm < .9){
                return LedData.LOW_RESISTANCE_PROBLEM
            }
            val finalResistorValue = addKiloMegaGigaSuffix(resistorOhm)
            val resistorPower = convertToStandardPower(powerWatt)
            //logging
            Log.i("result raw", resistorOhm.toString() + "ohm, Rating:" + powerWatt.toString())
            Log.i("result", finalResistorValue + "ohm, Rating:" + resistorOhm)
            //end
            return Result(finalResistorValue, " ",  resistorPower)
        }else{
            return LedData.VOLTAGE_PROBLEM  // 2 for voltage shortage.
        }
    }
    private fun greaterThanZero(input: Double):Boolean{
        if(input > 0)  return true
        return false
    }
    private fun greaterThanOne(input: Int):Boolean{
        if(input > 1)  return true
        return false
    }

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
        val limits = arrayOf<Double>( 1.0 ,1.1, 1.2, 1.3, 1.5, 1.6, 1.8, 2.0, 2.2, 2.4, 2.7, 3.0, 3.3, 3.6, 3.9, 4.3, 4.7, 5.1, 5.6, 6.2, 6.8, 7.5, 8.2, 9.1)
        val format = arrayOf<String>("1.1", "1.2", "1.3", "1.5", "1.6", "1.8", "2.0", "2.2", "2.4", "2.7", "3.0", "3.3", "3.6", "3.9", "4.3", "4.7", "5.1", "5.6", "6.2", "6.8", "7.5", "8.2", "9.1", "10")
        val cf = ChoiceFormat(limits.toDoubleArray(), format)
//        Log.i("generate:", cf.format(resistor))
        return cf.format(resistor)
    }


}