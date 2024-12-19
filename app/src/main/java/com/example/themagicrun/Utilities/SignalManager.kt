package com.example.themagicrun.Utilities

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import java.lang.ref.WeakReference

class SignalManager private constructor(context: Context) {
    private val contextRef = WeakReference(context)

    fun vibrate() {
        contextRef.get()?.let { context ->
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { //בדיקה של הגירסה של המכשיר
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(500)
            }
        }
    }

    fun toast(text: String) {
        contextRef.get()?.let { context ->
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @Volatile
        private var instance: SignalManager? = null

        fun init(context: Context) {
            instance ?: synchronized(this) {
                instance ?: SignalManager(context).also { instance = it }
            }
        }

        fun getInstance(): SignalManager {
            return instance ?: throw IllegalStateException(
                "SignalManager must be initialized before use"
            )
        }
    }
}