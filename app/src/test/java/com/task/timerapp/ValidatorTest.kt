package com.task.timerapp

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ValidatorTest{

    @Test
    fun whenInputIsValid() {
        val hour = "20"
        val min = "2"
        val sec = "20"
        val result = Validator.validateInput(hour, min, sec)
        assertThat(result).isEqualTo(true)

    }
}