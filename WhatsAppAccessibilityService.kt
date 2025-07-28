package com.example.whatsapptimer

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class WhatsAppAccessibilityService : AccessibilityService() {
    
    companion object {
        private const val TAG = "WhatsAppAccessibility"
        private const val WHATSAPP_PACKAGE = "com.whatsapp"
        
        // Common button text patterns for hanging up calls
        private val HANG_UP_PATTERNS = arrayOf(
            "end call",
            "hang up",
            "decline",
            "reject",
            "end",
            "cancel call"
        )
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d(TAG, "WhatsApp Accessibility Service connected")
        
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or 
                        AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED or
                        AccessibilityEvent.TYPE_VIEW_CLICKED
            
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                   AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                   AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            
            packageNames = arrayOf(WHATSAPP_PACKAGE)
            notificationTimeout = 100
        }
        
        serviceInfo = info
    }
    
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let { accessibilityEvent ->
            if (accessibilityEvent.packageName == WHATSAPP_PACKAGE) {
                Log.d(TAG, "WhatsApp event detected: ${accessibilityEvent.eventType}")
                
                when (accessibilityEvent.eventType) {
                    AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                        handleWindowStateChanged(accessibilityEvent)
                    }
                    AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                        // Handle content changes if needed
                    }
                }
            }
        }
    }
    
    private fun handleWindowStateChanged(event: AccessibilityEvent) {
        val className = event.className?.toString()
        Log.d(TAG, "Window state changed: $className")
        
        // Check if we're in a call screen
        if (isInCallScreen(className)) {
            Log.d(TAG, "Detected WhatsApp call screen")
        }
    }
    
    private fun isInCallScreen(className: String?): Boolean {
        // Common WhatsApp call activity class names
        val callScreenClasses = arrayOf(
            "com.whatsapp.voipcalling.VoipActivityV2",
            "com.whatsapp.calling.callscreen.CallScreenActivity",
            "com.whatsapp.VoipCallingActivity"
        )
        
        return callScreenClasses.any { className?.contains(it) == true }
    }
    
    fun attemptToHangUpCall(): Boolean {
        val rootNode = rootInActiveWindow ?: return false
        
        return try {
            Log.d(TAG, "Attempting to find hang-up button")
            
            // Method 1: Look for buttons with hang-up related text
            val hangUpButton = findHangUpButton(rootNode)
            if (hangUpButton != null) {
                Log.d(TAG, "Found hang-up button, attempting to click")
                val success = hangUpButton.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                hangUpButton.recycle()
                return success
            }
            
            // Method 2: Look for buttons with specific resource IDs
            val hangUpById = findHangUpButtonById(rootNode)
            if (hangUpById != null) {
                Log.d(TAG, "Found hang-up button by ID, attempting to click")
                val success = hangUpById.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                hangUpById.recycle()
                return success
            }
            
            // Method 3: Look for clickable elements in call screen
            val callButtons = findCallButtons(rootNode)
            for (button in callButtons) {
                val contentDesc = button.contentDescription?.toString()?.lowercase()
                val text = button.text?.toString()?.lowercase()
                
                if (isHangUpButton(contentDesc) || isHangUpButton(text)) {
                    Log.d(TAG, "Found potential hang-up button: $contentDesc / $text")
                    val success = button.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    button.recycle()
                    if (success) return true
                }
            }
            
            Log.w(TAG, "No hang-up button found")
            false
            
        } catch (e: Exception) {
            Log.e(TAG, "Error attempting to hang up call", e)
            false
        } finally {
            rootNode.recycle()
        }
    }
    
    private fun findHangUpButton(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        return findNodeWithText(rootNode, HANG_UP_PATTERNS)
    }
    
    private fun findHangUpButtonById(rootNode: AccessibilityNodeInfo): AccessibilityNodeInfo? {
        // Common resource IDs for hang-up buttons in WhatsApp
        val hangUpIds = arrayOf(
            "com.whatsapp:id/call_hang_up",
            "com.whatsapp:id/end_call_btn",
            "com.whatsapp:id/decline_btn",
            "com.whatsapp:id/call_end"
        )
        
        for (id in hangUpIds) {
            val nodes = rootNode.findAccessibilityNodeInfosByViewId(id)
            if (nodes.isNotEmpty()) {
                val node = nodes[0]
                // Clean up other nodes
                for (i in 1 until nodes.size) {
                    nodes[i].recycle()
                }
                return node
            }
        }
        return null
    }
    
    private fun findCallButtons(rootNode: AccessibilityNodeInfo): List<AccessibilityNodeInfo> {
        val buttons = mutableListOf<AccessibilityNodeInfo>()
        findClickableNodes(rootNode, buttons)
        return buttons
    }
    
    private fun findClickableNodes(node: AccessibilityNodeInfo, result: MutableList<AccessibilityNodeInfo>) {
        if (node.isClickable) {
            result.add(AccessibilityNodeInfo.obtain(node))
        }
        
        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            child?.let {
                findClickableNodes(it, result)
                it.recycle()
            }
        }
    }
    
    private fun findNodeWithText(rootNode: AccessibilityNodeInfo, patterns: Array<String>): AccessibilityNodeInfo? {
        for (pattern in patterns) {
            val nodes = rootNode.findAccessibilityNodeInfosByText(pattern)
            if (nodes.isNotEmpty()) {
                val node = nodes[0]
                // Clean up other nodes
                for (i in 1 until nodes.size) {
                    nodes[i].recycle()
                }
                return node
            }
        }
        return null
    }
    
    private fun isHangUpButton(text: String?): Boolean {
        if (text.isNullOrBlank()) return false
        val lowerText = text.lowercase()
        return HANG_UP_PATTERNS.any { pattern ->
            lowerText.contains(pattern)
        }
    }
    
    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Accessibility service destroyed")
    }
}