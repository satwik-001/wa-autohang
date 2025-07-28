
package com.example.autohangup

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                println("Seconds remaining: ${millisUntilFinished / 1000}")
            }

            override fun onFinish() {
                println("Time's up! (You would end call here with AccessibilityService)")
            }
        }
        timer.start()
    }
}
