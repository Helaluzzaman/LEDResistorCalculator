package com.hbsoft.ledresistorcalculator.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.preference.PreferenceManager
import com.hbsoft.ledresistorcalculator.R
import com.hbsoft.ledresistorcalculator.data.CalculationData
import com.hbsoft.ledresistorcalculator.data.Led
import com.hbsoft.ledresistorcalculator.data.LedData
import com.hbsoft.ledresistorcalculator.viewModel.LaunchViewModel
import java.lang.Exception

class LaunchFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDarkMode()
    }

    val mLaunchViewModel: LaunchViewModel by viewModels()
    lateinit var sLedColor: Spinner
    lateinit var etInputVoltage: EditText   // input
    lateinit var resultBox: CardView
    lateinit var bCalculate: Button
    lateinit var tvResult: TextView        // result
    lateinit var tvSuggestion: TextView    // suggestion
    lateinit var etForwardVoltage: EditText  // input
    lateinit var etCurrentMax : EditText    // input
    lateinit var rgConnection: RadioGroup
    lateinit var llExtraData: LinearLayout    // input [1]
    lateinit var schametics : ImageView

    // variables
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
        resultBox = view.findViewById(R.id.cardView)
        tvResult = view.findViewById(R.id.tv_result)
        tvSuggestion = view.findViewById(R.id.tv_suggestion)
        rgConnection = view.findViewById(R.id.radioGroup)
        llExtraData = view.findViewById(R.id.ll_led_number)
        schametics = view.findViewById(R.id.schematics)

        resultBox.setOnLongClickListener {
            findNavController().navigate(R.id.helpFragment)
            false
        }
        setSpinner()
        rgConnection.setOnCheckedChangeListener(mLaunchViewModel.radioGroupListener)
        bCalculate.setOnClickListener{

            val inputVoltageText = etInputVoltage.text.toString()
            val ledNumberText = (llExtraData[1] as EditText).text.toString()
            val forwardVoltageText = etForwardVoltage.text.toString()
            val currentMaxText = etCurrentMax.text.toString()
            try {
                val inputVoltage = inputVoltageText.toDouble()
                val ledNumber = ledNumberText.toInt()
                val forwardVoltage = forwardVoltageText.toDouble()
                val currentMax = currentMaxText.toDouble()
                val calculationData = CalculationData(inputVoltage,
                    ledNumber,
                    mLaunchViewModel.currentConnection.value!!,
                    currentLed.apply {
                        this.currentMax_mA = currentMax
                        this.forwardVoltage_V = forwardVoltage
                    }
                )
                val result = mLaunchViewModel.calculateResult(calculationData)
                showAlertDialogIfNeeded(result)
//                Toast.makeText(requireContext(), "Code: $result", Toast.LENGTH_SHORT).show()
                Log.i("result code",result.toString() )
            }catch (exception: Exception){
                Log.i("input", exception.toString())
                showAlertDialog("Empty Input!", "Input voltage or Led can not be empty." )
            }
        }
        setUiData()
        return view
    }

    private fun showAlertDialogIfNeeded(result: Any) {
        when(result){
            LedData.VOLTAGE_PROBLEM -> showAlertDialog("Insufficient Voltage!", "Voltage is not enough.")
            LedData.LED_NUMBER_PROBLEM -> showAlertDialog("Insufficient Led!", "At least two LEDs need for this calculation.")
            LedData.ZERO_PROBLEM -> showAlertDialog("Zero!", "You have put Zero in Input.")
            LedData.LOW_RESISTANCE_PROBLEM -> showAlertDialog("Limited Voltage!", "Voltage is not enough or Too many LEDs.")

        }
    }

    private fun setUiData() {

        mLaunchViewModel.currentLed.observe(viewLifecycleOwner,{
            currentLed = it
            etForwardVoltage.setText(it.forwardVoltage_V.toString())
            etCurrentMax.setText(it.currentMax_mA.toString())
        })
        mLaunchViewModel.CustomLed.observe(viewLifecycleOwner, {
            if(it){
                etForwardVoltage.isEnabled = true
                etCurrentMax.isEnabled = true
            }else{
                etForwardVoltage.isEnabled = false
                etCurrentMax.isEnabled = false
            }
        })
        mLaunchViewModel.currentConnection.observe(viewLifecycleOwner,{
            when(it){
                LedData.SINGLE -> {
                    llExtraData.visibility = View.GONE
                    schametics.setImageResource(R.drawable.ic_schematics)
                }
                LedData.SERIES -> {
                    llExtraData.visibility = View.VISIBLE
                    schametics.setImageResource(R.drawable.ic_schematics_series)
                }
                LedData.PARALLEL -> {
                    llExtraData.visibility = View.VISIBLE
                    schametics.setImageResource(R.drawable.ic_schematics_parallel)
                }
            }
        })
        mLaunchViewModel.fullResult.observe(viewLifecycleOwner, {
            tvResult.text = it.resistor
            val suggestion = "Suggested: " + it.standardResistor +"  " + it.resistorPower
            tvSuggestion.text = suggestion
        })

    }

    fun setSpinner(){
        val ledNameArray = mLaunchViewModel.LedNameList
        val Adapter = ArrayAdapter(requireContext(),R.layout.support_simple_spinner_dropdown_item,ledNameArray)
        sLedColor.adapter = Adapter
        sLedColor.onItemSelectedListener = mLaunchViewModel.listener
    }
    private fun showAlertDialog(title: String, info: String){
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(info)
        builder.setPositiveButton("Okay") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.launch_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.onNavDestinationSelected(findNavController())
//        when(item.itemId){
////            R.id.menu_about -> findNavController().navigate(R.id.action_launchFragment_to_aboutFragment)
////            R.id.menu_help -> findNavController().navigate(R.id.action_launchFragment_to_helpFragment)
//        }
        return super.onOptionsItemSelected(item)
    }
    private fun setDarkMode() {
        val sharedPreferences  = PreferenceManager.getDefaultSharedPreferences(requireContext())
        if(sharedPreferences.getBoolean(getString(R.string.Mode_key), false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}