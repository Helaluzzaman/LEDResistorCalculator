package com.hbsoft.ledresistorcalculator.viewModel

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hbsoft.ledresistorcalculator.data.Led
import com.hbsoft.ledresistorcalculator.data.repository.Repository
import java.text.FieldPosition

class LaunchViewModel(application: Application): AndroidViewModel(application) {
    val repository: Repository = Repository()
    val LedDataList =repository.getAllLedData()
    val LedNameList = repository.getAllLedNames()
    // working variables
    var currentLed= MutableLiveData<Led>()
    var currentConnection =  MutableLiveData<String>()
    var rawResultOhm = MutableLiveData<Double>()

    val listener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Toast.makeText(application.applicationContext, "${parent!!.getItemAtPosition(position)}, $position", Toast.LENGTH_SHORT).show()

            setLed(position)
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }

    private fun setLed(position: Int) {
        currentLed.value = LedDataList[position]
        Log.i("Led", currentLed.value.toString())
    }

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

}