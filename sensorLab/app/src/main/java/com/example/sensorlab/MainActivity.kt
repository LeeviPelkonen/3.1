package com.example.sensorlab

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sm: SensorManager
    private var sAcceleration: Sensor? = null
    private val df = DecimalFormat("#.##")
    private var topValue: Float = 1.0.toFloat()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        df.roundingMode = RoundingMode.CEILING
        reset.setOnClickListener { reset() }
        sm = getSystemService(Context.SENSOR_SERVICE) as
                SensorManager
        sAcceleration = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0?.sensor == sAcceleration) {
            val a = p0?.values?.get(0)
            if(a != null && a > -0.000000001) {
                if (a > topValue) {
                    topValue = a.toFloat()
                    TextView2.text = df.format(topValue).toString()
                }
                TextView.text = df.format(a).toString()
            }
        }
    }
    private fun reset(){
        topValue = 0.0.toFloat()
        TextView2.text = 0.0.toString()
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
         // Do something if accuracy change
        }

    override fun onResume() {
        super.onResume()
        sAcceleration?.also {
            sm.registerListener(this, it,
            SensorManager.SENSOR_DELAY_NORMAL)
            }
        }

    override fun onPause() {
        super.onPause()
        sm.unregisterListener(this)
    }
}
