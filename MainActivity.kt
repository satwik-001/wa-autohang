package com.example.whatsapptimer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    
    private lateinit var timeInput: EditText
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var timerDisplay: TextView
    private lateinit var statusText: TextView
    
    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupClickListeners()
        checkAccessibilityService()
    }
    
    private fun initViews() {
        timeInput = findViewById(R.id.timeInput)
        startButton = findViewById(R.id.startButton)
        stopButton = findViewById(R.id.stopButton)
        timerDisplay = findViewById(R.id.timerDisplay)
        statusText = findViewById(R.id.statusText)
        
        stopButton.isEnabled = false
        timerDisplay.text = "00:00"
        statusText.text = "Ready to start timer"
    }
    
    private fun setupClickListeners() {
        startButton.setOnClickListener {
            startTimer()
        }
        
        stopButton.setOnClickListener {
            stopTimer()
        }
    }
    
    private fun startTimer() {
        val minutesText = timeInput.text.toString().trim()
        
        if (minutesText.isEmpty()) {
            Toast.makeText(this, "Please enter time in minutes", Toast.LENGTH_SHORT).show()
            return
        }
        
        val minutes = minutesText.toIntOrNull()
        if (minutes == null || minutes <= 0) {
            Toast.makeText(this, "Please enter a valid positive number", Toast.LENGTH_SHORT).show()
            return
        }
        
        val totalMillis = minutes * 60 * 1000L
        
        countDownTimer = object : CountDownTimer(totalMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                val mins = secondsRemaining / 60
                val secs = secondsRemaining % 60
                timerDisplay.text = String.format("%02d:%02d", mins, secs)
                statusText.text = "Timer running..."
            }
            
            override fun onFinish() {
                timerDisplay.text = "00:00"
                statusText.text = "Timer finished! Attempting to hang up WhatsApp call..."
                isTimerRunning = false
                startButton.isEnabled = true
                stopButton.isEnabled = false
                
                // Trigger WhatsApp call hang up
                hangUpWhatsAppCall()
            }
        }
        
        countDownTimer?.start()
        isTimerRunning = true
        startButton.isEnabled = false
        stopButton.isEnabled = true
        statusText.text = "Timer started for $minutes minutes"
    }
    
    private fun stopTimer() {
        countDownTimer?.cancel()
        countDownTimer = null
        isTimerRunning = false
        startButton.isEnabled = true
        stopButton.isEnabled = false
        timerDisplay.text = "00:00"
        statusText.text = "Timer stopped"
        Toast.makeText(this, "Timer stopped", Toast.LENGTH_SHORT).show()
    }
    
    private fun hangUpWhatsAppCall() {
        // This is where the WhatsApp call hang-up logic would go
        // For now, we'll show a toast and log the action
        Toast.makeText(this, "Simulating WhatsApp call hang-up...", Toast.LENGTH_LONG).show()
        
        // TODO: Implement actual WhatsApp call hang-up using:
        // 1. Accessibility Service to find and click hang-up button
        // 2. Or Bluetooth HID emulation to simulate hardware button
        // 3. Or root-based automation (requires root access)
        
        statusText.text = "WhatsApp hang-up simulation completed"
        
        // Placeholder for actual implementation
        simulateCallHangUp()
    }
    
    private fun simulateCallHangUp() {
        // Placeholder method for WhatsApp call hang-up
        // In a real implementation, this would:
        // - Use AccessibilityService to find WhatsApp call UI elements
        // - Simulate tap on hang-up button
        // - Or use other automation methods
        
        Toast.makeText(this, "Call hang-up action would be executed here", Toast.LENGTH_SHORT).show()
    }
    
    private fun checkAccessibilityService() {
        // Check if accessibility service is enabled (for future WhatsApp automation)
        val accessibilityEnabled = Settings.Secure.getInt(
            contentResolver,
            Settings.Secure.ACCESSIBILITY_ENABLED,
            0
        )
        
        if (accessibilityEnabled == 0) {
            Toast.makeText(
                this,
                "Note: Accessibility Service not enabled. WhatsApp automation will be limited.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}