package com.hbsoft.ledresistorcalculator.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hbsoft.ledresistorcalculator.R
import com.hbsoft.ledresistorcalculator.data.CalculationData
import com.hbsoft.ledresistorcalculator.data.Led
import com.hbsoft.ledresistorcalculator.data.LedData
import com.hbsoft.ledresistorcalculator.data.repository.Repository
import java.text.FieldPosition

class LaunchViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository = Repository()
    val LedDataList =repository.getAllLedData()
    val LedNameList = repository.getAllLedNames()
    // working variables
    var currentLed= MutableLiveData<Led>()
    var currentConnection =  MutableLiveData<String>(LedData.SINGLE)
    var rawResultOhm = MutableLiveData<Double>()

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
    fun calculateResult(calculationData: CalculationData): Int{
        val inputVoltage = calculationData.input_voltage
        val ledNumber = calculationData.led_number
        val forwardVoltage = calculationData.led.forwardVoltage_V
        val currentMax = calculationData.led.currentMax_mA
        if(inputVoltage.equals(0.0)  || forwardVoltage.equals(0.0) || currentMax.equals(0.0)){
            return 0   // zero for any input value is filled with zero value
        }else{
            when(calculationData.connection){
                LedData.SINGLE -> return singleConnectionCalculation(inputVoltage, forwardVoltage, currentMax)
                LedData.SERIES -> return seriesConnectionCalculation(inputVoltage, ledNumber, forwardVoltage, currentMax)
                LedData.PARALLEL -> return parallelConnectionCalculation(inputVoltage, ledNumber, forwardVoltage, currentMax)
            }
        }
        Toast.makeText(getApplication(), calculationData.toString(), Toast.LENGTH_SHORT).show()
        Log.i("calculationData", calculationData.toString())
        return 2
    }

    private fun parallelConnectionCalculation(
        inputVoltage: Double,
        ledNumber: Int,
        forwardVoltage: Double,
        currentMax: Double
    ): Int {
        val voltage = inputVoltage - forwardVoltage
        val current = currentMax* ledNumber
        if(greaterThanZero(voltage)){
            val resistorOhm = (voltage/current)*1000
            val powerWatt = voltage*current/1000
            rawResultOhm.value = resistorOhm     // fow testing
            Log.i("result", resistorOhm.toString() + ", " + powerWatt.toString())
            return 1    // successful result

        }else{
            return 2  // 2 for voltage shortage.
        }
    }

    private fun seriesConnectionCalculation(
        inputVoltage: Double,
        ledNumber: Int,
        forwardVoltage: Double,
        currentMax: Double
    ): Int {
        val voltage = inputVoltage - (forwardVoltage*ledNumber)
        val current = currentMax
        if(greaterThanZero(voltage)){
            val resistorOhm = (voltage/current)*1000
            val powerWatt = voltage* current/1000
            rawResultOhm.value = resistorOhm     // fow testing
            Log.i("result", resistorOhm.toString() + ", " + powerWatt.toString())
            return 1    // successful result

        }else{
            return 2  // 2 for voltage shortage.
        }
    }

    private fun singleConnectionCalculation(
        inputVoltage: Double,
        forwardVoltage: Double,
        currentMax: Double
    ): Int {
            val voltage = inputVoltage - forwardVoltage
            val current = currentMax
            if(greaterThanZero(voltage)){
                val resistorOhm = (voltage/current)*1000
                val powerWatt = voltage* current/1000
                rawResultOhm.value = resistorOhm     // fow testing
                Log.i("result", resistorOhm.toString() + ", " + powerWatt.toString())
                return 1    // successful result

            }else{
                return 2  // 2 for voltage shortage.
        }
    }


    // will be removed
    fun calculateResult(inputVoltage:Double){
        val fv = currentLed.value!!.forwardVoltage_V
        val c = currentLed.value!!.currentMax_mA
        val resultInOhm = ((inputVoltage - fv)/ c)*1000
        Log.i("result Ohm", resultInOhm.toString())
        rawResultOhm.value = resultInOhm
    }

    fun validator(inputVoltage: Double, ForwardVoltage: Double): Boolean{
        return inputVoltage > ForwardVoltage
    }
    fun greaterThanZero(input: Double):Boolean{
        if(input > 0)  return true
        return false
    }

}