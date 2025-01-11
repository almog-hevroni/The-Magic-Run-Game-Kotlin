package com.example.themagicrun

import com.example.themagicrun.Utilities.GameSpeed
import java.util.Timer
import java.util.TimerTask

class GameTimer(
    private var interval: Long = 700,
    private val onTick: () -> Unit
) {
    private var timer: Timer? = null
    var isRunning = false
        private set

    private var baseSpeed: GameSpeed? = null  // שינוי ל-nullable

    fun setBaseSpeed(speed: GameSpeed) {
        baseSpeed = speed
        setSpeed(speed)
    }

    fun setSpeed(gameSpeed: GameSpeed) {
        interval = when (gameSpeed) {
            GameSpeed.VERY_SLOW -> 1000
            GameSpeed.SLOW -> 700
            GameSpeed.FAST -> 400
            GameSpeed.VERY_FAST -> 200
        }
        if (isRunning) {
            stop()
            start()
        }
    }

    fun adjustSpeedBasedOnTilt(isTiltingForward: Boolean) {
        baseSpeed?.let { currentBaseSpeed ->  // בדיקה שיש ערך בסיסי שנבחר
            val newSpeed = when (currentBaseSpeed) {
                GameSpeed.SLOW -> {
                    if (isTiltingForward) GameSpeed.FAST
                    else GameSpeed.VERY_SLOW
                }
                GameSpeed.FAST -> {
                    if (isTiltingForward) GameSpeed.VERY_FAST
                    else GameSpeed.SLOW
                }
                else -> currentBaseSpeed
            }
            setSpeed(newSpeed)
        }
    }

    fun start() {
        if (!isRunning) {
            isRunning = true
            timer = Timer().apply {
                schedule(object : TimerTask() {
                    override fun run() {
                        onTick()
                    }
                }, 0, interval)
            }
        }
    }

    fun stop() {
        if (isRunning) {
            isRunning = false
            timer?.cancel()
            timer = null
        }
    }
}