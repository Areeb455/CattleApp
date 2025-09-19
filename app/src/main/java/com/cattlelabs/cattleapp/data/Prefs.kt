package com.cattlelabs.cattleapp.data

import android.content.Context

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("cattle_prefs", Context.MODE_PRIVATE)

    fun saveSession(userId: String, userName: String, phoneNumber: String, location: String) {
        prefs.edit()
            .putString("user_id", userId)
            .putString("user_name", userName)
            .putString("phone_number", phoneNumber)
            .putString("location", location)
            .apply()
    }

    fun getSession(): String? {
        val userId = prefs.getString("user_id", null)
        return userId
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun getUserId(): String? {
        return prefs.getString("user_id", null)
    }

    fun getUserName(): String? {
        return prefs.getString("user_name", "Guest")
    }

    fun getLocation(): String? {
        return prefs.getString("location", null)
    }

    fun getPhoneNumber(): String? {
        return prefs.getString("phone_number", null)
    }

}