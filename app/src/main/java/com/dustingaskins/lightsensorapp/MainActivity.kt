package com.dustingaskins.lightsensorapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null

    private lateinit var tvLightLevel: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLightLevel = findViewById(R.id.tvLightLevel)
        tvDescription = findViewById(R.id.tvDescription)
        tvStatus = findViewById(R.id.tvStatus)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        if (lightSensor == null) {
            tvStatus.text = "Light sensor not available.\nTry using Extended Controls in the emulator."
            tvLightLevel.text = "N/A"
        } else {
            tvStatus.text = "Light sensor ready ✓"
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            val lux = event.values[0]

            tvLightLevel.text = "${lux.toInt()} lux"

            val description = when (lux.toInt()) {
                in 0..10 -> "Pitch black"
                in 11..50 -> "Very dark"
                in 51..200 -> "Dim room"
                in 201..500 -> "Normal indoor lighting"
                in 501..2000 -> "Bright indoor"
                in 2001..10000 -> "Daylight / bright room"
                in 10001..25000 -> "Direct sunlight"
                else -> "Extremely bright!"
            }
            tvDescription.text = description
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}