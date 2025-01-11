package com.example.themagicrun.Utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.themagicrun.interfaces.TiltCallback

class TiltDetector(context: Context, private val tiltCallback: TiltCallback?) {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private lateinit var sensorEventListener: SensorEventListener

    private var timestamp: Long = 0L
    private val TILT_THRESHOLD = 3.0f
    private val SENSOR_DELAY = 500L // חצי שנייה

    init {
        initEventListener()
    }

    private fun initEventListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0] // הטיה ימינה-שמאלה
                val y = event.values[1] // הטיה קדימה-אחורה
                if (System.currentTimeMillis() - timestamp >= SENSOR_DELAY) {
                    timestamp = System.currentTimeMillis()

                    // טיפול בהטיה לצדדים עבור תנועה
                    if (kotlin.math.abs(x) >= TILT_THRESHOLD) {
                        tiltCallback?.tiltX(x)
                    }

                    // טיפול בהטיה קדימה-אחורה עבור שליטה במהירות
                    if (kotlin.math.abs(y) >= TILT_THRESHOLD) {
                        tiltCallback?.tiltY(y)
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // לא נדרש שינוי
            }
        }
    }

    fun start() {
        sensor?.let {
            sensorManager.registerListener(
                sensorEventListener,
                it,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun stop() {
        sensorManager.unregisterListener(sensorEventListener)
    }
}