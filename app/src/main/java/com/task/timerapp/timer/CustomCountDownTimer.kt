package com.task.timerapp.timer

import android.os.CountDownTimer

class CustomCountDownTimer(private var viewModel: TimerViewModel) {
    private var timer: CountDownTimer? = null
    private var isTimerActive = false

    fun startTimer(time_in_seconds: Long, position: Int) {
        isTimerActive = true
        timer = object : CountDownTimer(time_in_seconds*1000, 1000) {
            override fun onFinish() {
                viewModel.endTime(position)

            }

            override fun onTick(millisUntilFinished: Long) {
                if (isTimerActive) {
                    viewModel.updateTime(position)
                } else {
                    cancel()
                }
            }
        }
        timer?.start()

    }

    fun stopTimer() {
        isTimerActive = false
        if (timer != null) {
            timer?.cancel()
            timer = null
        }
    }

}