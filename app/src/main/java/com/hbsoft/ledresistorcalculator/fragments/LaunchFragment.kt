package com.hbsoft.ledresistorcalculator.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import com.hbsoft.ledresistorcalculator.R
import com.hbsoft.ledresistorcalculator.viewModel.LaunchViewModel
import org.w3c.dom.Text

class LaunchFragment : Fragment() {

    val mLaunchViewModel: LaunchViewModel by viewModels()
    lateinit var sLedColor: Spinner
    lateinit var etInputVoltage: EditText
    lateinit var bCalculate: Button
    lateinit var tvResult: TextView

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
        tvResult = view.findViewById(R.id.tv_result)
        setSpinner()
        return view
    }
    fun setSpinner(){
        val ledNameArray = mLaunchViewModel.LedNameList
        val Adapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,ledNameArray)
        sLedColor.adapter = Adapter
    }


    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
    }
}