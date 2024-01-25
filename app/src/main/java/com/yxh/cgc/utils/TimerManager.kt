package com.yxh.cgc.utils

import java.util.Timer
import java.util.TimerTask

class TimerManager {
    private var timer: Timer? = null

    fun startTimer(delay: Long, period: Long, timerTask: TimerTask) {
        clearTimer()
        timer = Timer()
        timer?.schedule(timerTask, delay, period)
    }

    fun clearTimer() {
        timer?.cancel()
        timer = null
    }

}