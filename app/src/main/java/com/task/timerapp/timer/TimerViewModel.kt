package com.task.timerapp.timer

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    var hour = mutableStateOf("")
    var min = mutableStateOf("")
    var sec = mutableStateOf("")
    var timersList = mutableStateListOf<String>()
    val customCountDownTimer = CustomCountDownTimer(this@TimerViewModel)


    fun updateHour(hour: String) {
        this.hour.value = hour
    }

    fun updateMin(min: String) {
        this.min.value = min
    }

    fun updateSec(sec: String) {
        this.sec.value = sec
    }

    fun updateTimerList() {
        var insertedTime = ""
        var hourTime = "00"
        var minTime = "00"
        var secTime = "00"

        if (hour.value.isEmpty().not()) {
            hourTime = String.format("%02d", hour.value.toInt())
        } else {
            hour.value = "00"
        }

        if (min.value.isEmpty().not()) {
            minTime = String.format("%02d", min.value.toInt())
        } else {
            min.value = "00"
        }

        if (sec.value.isEmpty().not()) {
            secTime = String.format("%02d", sec.value.toInt())
        } else {
            sec.value = "00"
        }

        insertedTime = "$hourTime:$minTime:$secTime"
        this.timersList.add(insertedTime) // Added Time in last so that we can pass actual position to CountDown Timer
        val totalSecond = hour.value.toLong() * 3600 + min.value.toLong() * 60 + sec.value.toLong()
        startTimer(timersList.size - 1, totalSecond)
        clearEntry()

    }

    private fun clearEntry() {
        hour.value = ""
        min.value = ""
        sec.value = ""
    }


    private fun startTimer(position: Int, time_in_seconds: Long) {
        viewModelScope.launch {
            customCountDownTimer.startTimer(time_in_seconds, position = position)
        }
    }

    fun stopTimer() {
        customCountDownTimer.stopTimer()
        viewModelScope.cancel()
    }

    fun updateTime(position: Int) {
        val updatedTime = calculateAndUpdateTime(timersList.get(position))
        timersList[position] = updatedTime
    }

    fun endTime(position: Int) {
        timersList[position] = "00:00:00"
    }

    fun calculateAndUpdateTime(time: String): String {
        var hr = time.substring(0, 2)
        var min = time.substring(3, 5)
        var sec = time.substring(6)
        if (sec.equals("00").not()) {
            sec = (sec.toInt() - 1).toString()
        } else if (min.equals("00").not()) {
            min = (min.toInt() - 1).toString()
            sec = "59"
        } else if (hr.equals("00").not()) {
            hr = (hr.toInt() - 1).toString()
            min = "59"
            sec = "59"
        }
        val newTime = String.format("%02d", hr.toInt()) + ":" + String.format(
            "%02d",
            min.toInt()
        ) + ":" + String.format("%02d", sec.toInt())
        return newTime
    }

}
