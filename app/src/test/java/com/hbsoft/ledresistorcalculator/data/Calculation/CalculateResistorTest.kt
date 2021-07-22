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
    fun generateSuggestion() {
        val input = listOf<Double>(1.0)
        val result = listOf("1.1", "1.3","1.5", "1.6", "2", "2.4")
        for ((i, item) in input.withIndex()) {
            Truth.assertThat(CalculateResistor.generateSuggestion(item)).isEqualTo(result[i])
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
        val result = Result("1.04K", " ", "1/2")
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
        val result = Result("845", " ", "1/2")
        Truth.assertThat(CalculateResistor.calculateResult(calculationData)).isEqualTo(result)
    }
}