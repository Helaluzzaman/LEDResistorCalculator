package com.hbsoft.ledresistorcalculator.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hbsoft.ledresistorcalculator.R
import com.hbsoft.ledresistorcalculator.data.Calculation.Calculate
import com.hbsoft.ledresistorcalculator.data.CalculationData
import com.hbsoft.ledresistorcalculator.data.Led
import com.hbsoft.ledresistorcalculator.data.LedData
import com.hbsoft.ledresistorcalculator.data.Result
import com.hbsoft.ledresistorcalculator.data.repository.Repository
import java.math.RoundingMode
import java.text.NumberFormat

class LaunchViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository = Repository()
    val LedDataList =repository.getAllLedData()
    val LedNameList = repository.getAllLedNames()
    // working variables
    var currentLed= MutableLiveData<Led>()
    var currentConnection =  MutableLiveData<String>(LedData.SINGLE)
    var fullResult = MutableLiveData<Result>()

    fun setFinalResult(resistor: String, suggestion: String){
        val result = Result(resistor, suggestion)
        fullResult.value = result
    }

    val listener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            Toast.makeText(application.applicationContext, "${parent!!.getItemAtPosition(position)}, $position", Toast.LENGTH_SHORT).show()
            setLed(position)
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
    val radioGroupListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        when(checkedId){
            R.id.rb_single -> currentConnection.value = LedData.SINGLE
            R.id.rb_series -> currentConnection.value = LedData.SERIES
            R.id.rb_parallel -> currentConnection.value = LedData.PARALLEL
        }
    }

    private fun setLed(position: Int) {
        currentLed.value = LedDataList[position]
        Log.i("Led", currentLed.value.toString())
    }

    // all calculation will be here
    fun calculateResult(calculationData: CalculationData): Any{
        Log.i("calculationData", calculationData.toString())

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
            val finalResistorValue = addKiloMegaGigaSuffix(resistorOhm) + "Ω"
            val suggestion = "Suggestion: ${Calculate.convertToStandardPower(powerWatt)}W"
            setFinalResult(finalResistorValue, suggestion)
            Log.i("result raw", resistorOhm.toString() + "ohm, Rating:" + powerWatt.toString())
            Log.i("result", finalResistorValue + "ohm, Rating:" + suggestion)
            return LedData.SUCCESS   // successful result
        }else{
            return LedData.VOLTAGE_PROBLEM  // 2 for voltage shortage.
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




    private fun greaterThanZero(input: Double):Boolean{
        if(input > 0)  return true
        return false
    }
    fun greaterThanOne(input: Int):Boolean{
        if(input > 1)  return true
        return false
    }
}