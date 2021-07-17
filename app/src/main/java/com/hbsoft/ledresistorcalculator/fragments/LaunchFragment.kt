package com.hbsoft.ledresistorcalculator.fragments

import android.os.Bundle
import android.os.Trace
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.fragment.app.viewModels
import com.hbsoft.ledresistorcalculator.R
import com.hbsoft.ledresistorcalculator.data.Led
import com.hbsoft.ledresistorcalculator.viewModel.LaunchViewModel
import org.w3c.dom.Text
import java.lang.Exception

class LaunchFragment : Fragment() {

    val mLaunchViewModel: LaunchViewModel by viewModels()
    lateinit var sLedColor: Spinner
    lateinit var etInputVoltage: EditText
    lateinit var bCalculate: Button
    lateinit var tvResult: TextView
    lateinit var etForwardVoltage: EditText
    lateinit var etCurrentMax : EditText


    lateinit var  currentLed: Led

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        val view =  inflater.inflate(R.layout.fragment_launch, container, false)
        sLedColor = view.findViewById(R.id.s_led_color)
        etInputVoltage = view.findViewById(R.id.et_input_voltage)
        bCalculate = view.findViewById(R.id.b_calculate)
        etForwardVoltage = view.findViewById(R.id.et_forward_voltage)
        etCurrentMax = view.findViewById(R.id.et_current_max)
        tvResult = view.findViewById(R.id.tv_result)
        setSpinner()
        bCalculate.setOnClickListener{
            var inputVoltage: Double = 0.0
            val inputVoltageText = etInputVoltage.text.toString()
            try {
                inputVoltage = inputVoltageText.toDouble()
                val validateInputVoltage = mLaunchViewModel.validator(inputVoltage, currentLed.forwardVoltage_V)
                if(validateInputVoltage ){
                    mLaunchViewModel.calculateResult(inputVoltage)
                }else{
                    Toast.makeText(requireContext(), "Input voltage should be greater than Forward Voltage.", Toast.LENGTH_SHORT).show()
                }
            }catch (exception: Exception){
                Log.i("input", exception.toString())
                Toast.makeText(requireContext(), "Input voltage can not be empty", Toast.LENGTH_SHORT).show()
            }
            

        }
        setUiData()
        return view
    }

    private fun setUiData() {
        mLaunchViewModel.currentLed.observe(viewLifecycleOwner,{
            currentLed = it
            etForwardVoltage.setText(it.forwardVoltage_V.toString())
            etCurrentMax.setText(it.currentMax_mA.toString())
        })
        mLaunchViewModel.rawResultOhm.observe(viewLifecycleOwner, {
            tvResult.setText("Result: " + it.toString() + " ohm (raw result.)")
        })
    }

    fun setSpinner(){
        val ledNameArray = mLaunchViewModel.LedNameList
        val Adapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,ledNameArray)
        sLedColor.adapter = Adapter
        sLedColor.onItemSelectedListener = mLaunchViewModel.listener
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.launch_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

}