package com.hbsoft.ledresistorcalculator.data.Calculation

import com.google.common.truth.Truth
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
    }
}