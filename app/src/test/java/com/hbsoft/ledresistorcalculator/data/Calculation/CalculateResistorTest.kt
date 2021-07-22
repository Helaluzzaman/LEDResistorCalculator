package com.hbsoft.ledresistorcalculator.data.Calculation

import android.support.v4.media.MediaMetadataCompat
import com.google.common.truth.Truth
import com.hbsoft.ledresistorcalculator.data.CalculationData
import com.hbsoft.ledresistorcalculator.data.LedData
import com.hbsoft.ledresistorcalculator.data.Result
import org.junit.Assert.*

import org.junit.Test

class CalculateResistorTest {
    @Test
    fun addKiloMegaGigaSuffix() {
        val input = listOf<Double>(1234.4 , 12.0, 343.0, 21223434.89, 732947.002)
        val result = listOf("1.23K", "12", "343", "21.22M", "732.95K")
        for ((i, item) in input.withIndex()) {
            Truth.assertThat(CalculateResistor.addKiloMegaGigaSuffix(item)).isEqualTo(result[i])
        }
    }

    @Test
    fun generateSuggestion1() {
        val input = listOf<Double>(1.0, 1.27, 1.45,1.55, 1.9, 2.34, 2.56, 2.89, 3.25, 3.78, 2.0, 5.7, 8.9,9.0, 9.2, )
        val result = listOf<Number>(1.1, 1.3, 1.5,1.6, 2, 2.4, 2.7, 3, 3.3, 3.9, 2.2, 6.2, 9.1,9.1, 10)
        for ((i, item) in input.withIndex()) {
            Truth.assertThat(CalculateResistor.generateSuggestion(item)).isEqualTo(result[i].toString())
        }
    }
    @Test
    fun generateSuggestion3() {
        val input = listOf<Double>(100.2, 127.34, 145.0 ,155.0 , 190.0, 234.34, 256.0, 289.0, 325.0, 378.0, 200.0, 573.0, 890.0,900.0, 924.0)
        val result = listOf<Any>(110, 130, 150 ,160, 200, 240, 270, 300, 330, 390, 220, 620, 910,910, "1K")
        for ((i, item) in input.withIndex()) {
            Truth.assertThat(CalculateResistor.generateSuggestion(item)).isEqualTo(result[i].toString())
        }
    }

    @Test
    fun fullCalculation1(){
        val calculationData: CalculationData = CalculationData(
            22.9,
            1,
            LedData.SINGLE,
            LedData.getLedList()[1]
            )
        val result = Result("1.04K", "1.1K", "1/2")
        Truth.assertThat(CalculateResistor.calculateResult(calculationData)).isEqualTo(result)
    }
    @Test
    fun fullCalculation2(){
        val calculationData: CalculationData = CalculationData(
            22.9,
            3,
            LedData.SERIES,
            LedData.getLedList()[0]   // red led
        )
        val result = Result("845", "910", "1/2")
        Truth.assertThat(CalculateResistor.calculateResult(calculationData)).isEqualTo(result)
    }
}