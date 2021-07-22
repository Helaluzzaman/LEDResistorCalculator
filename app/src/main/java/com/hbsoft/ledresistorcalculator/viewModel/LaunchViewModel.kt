package com.hbsoft.ledresistorcalculator.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RadioGroup
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hbsoft.ledresistorcalculator.R
import com.hbsoft.ledresistorcalculator.data.Calculation.CalculateResistor
import com.hbsoft.ledresistorcalculator.data.CalculationData
import com.hbsoft.ledresistorcalculator.data.Led
import com.hbsoft.ledresistorcalculator.data.LedData
import com.hbsoft.ledresistorcalculator.data.Result
import com.hbsoft.ledresistorcalculator.data.repository.Repository

class LaunchViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository = Repository()
    val LedDataList =repository.getAllLedData()
    val LedNameList = repository.getAllLedNames()
    // working variables
    var currentLed= MutableLiveData<Led>()
    var currentConnection =  MutableLiveData<String>(LedData.SINGLE)
    var fullResult = MutableLiveData<Result>()
    val CustomLed = MutableLiveData<Boolean>(false)

    // unit adding to result and setting up result.
    private fun setResultWithUnit(resistor: String, standardResistor: String, resistorPower: String){
        val resistorWUnit = resistor + "Ω"
        val standardResistorWUnit = standardResistor + "Ω"
        val resistorPowerWUnit = resistorPower + "W"
        val fResult = Result(resistorWUnit,standardResistorWUnit,resistorPowerWUnit  )
        fullResult.value = fResult
    }
    private fun clearResult(){
        val resistorWUnit = "_"
        val standardResistorWUnit = ""
        val resistorPowerWUnit = ""
        val fResult = Result(resistorWUnit,standardResistorWUnit,resistorPowerWUnit  )
        fullResult.value = fResult
    }

    val listener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            Toast.makeText(application.applicationContext, "${parent!!.getItemAtPosition(position)}, $position", Toast.LENGTH_SHORT).show()
            clearResult()
            setLed(position)
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
    val radioGroupListener = RadioGroup.OnCheckedChangeListener { group, checkedId ->
        when(checkedId){
            R.id.rb_single -> {
                currentConnection.value = LedData.SINGLE
//                clearResult()
            }
            R.id.rb_series -> {
                currentConnection.value = LedData.SERIES
//                clearResult()
            }
            R.id.rb_parallel -> {
                currentConnection.value = LedData.PARALLEL
//                clearResult()
            }
        }
    }

    private fun setLed(position: Int) {
        currentLed.value = LedDataList[position]
        Log.i("Led", currentLed.value.toString())
        // set custom
        if(LedDataList[position].name == "Custom"){
            setCustomTrue()
        }else setCustomFalse()
    }
    fun setCustomTrue(){
        CustomLed.value = true
    }
    fun setCustomFalse(){
        CustomLed.value = false
    }

    // all calculation will be here
    fun calculateResult(calculationData: CalculationData): Any {
        val result =  CalculateResistor.calculateResult(calculationData)
        if(result is Result){
            setResultWithUnit(result.resistor, result.standardResistor, result.resistorPower)
            return LedData.SUCCESS   // successful result
        }
        return result
    }
}