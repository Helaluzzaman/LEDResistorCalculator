package com.hbsoft.ledresistorcalculator.data

data class CalculationData(
    var input_voltage: Double,
    var led_number: Int = 1,
    var connection: String = LedData.PARALLEL,
    val led: Led,
)

