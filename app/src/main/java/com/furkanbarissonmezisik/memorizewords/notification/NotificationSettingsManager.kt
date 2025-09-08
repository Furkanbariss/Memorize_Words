package com.furkanbarissonmezisik.memorizewords.notification

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class NotificationSettingsManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "notification_preferences",
        Context.MODE_PRIVATE
    )
    
    var isEnabled by mutableStateOf(getStoredEnabledState())
        private set
    
    var reminderHour by mutableStateOf(getStoredReminderHour())
        private set
    
    var reminderMinute by mutableStateOf(getStoredReminderMinute())
        private set
    
    fun updateEnabled(enabled: Boolean) {
        isEnabled = enabled
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }
    
    fun setReminderTime(hour: Int, minute: Int) {
        reminderHour = hour
        reminderMinute = minute
        prefs.edit()
            .putInt("reminder_hour", hour)
            .putInt("reminder_minute", minute)
            .apply()
    }
    
    private fun getStoredEnabledState(): Boolean {
        return prefs.getBoolean("notifications_enabled", false)
    }
    
    private fun getStoredReminderHour(): Int {
        return prefs.getInt("reminder_hour", 9) // Default 9 AM
    }
    
    private fun getStoredReminderMinute(): Int {
        return prefs.getInt("reminder_minute", 0) // Default 0 minutes
    }
    
    fun getFormattedTime(): String {
        val hour12 = if (reminderHour == 0) 12 else if (reminderHour > 12) reminderHour - 12 else reminderHour
        val amPm = if (reminderHour < 12) "AM" else "PM"
        val minuteStr = String.format("%02d", reminderMinute)
        return "$hour12:$minuteStr $amPm"
    }
}
