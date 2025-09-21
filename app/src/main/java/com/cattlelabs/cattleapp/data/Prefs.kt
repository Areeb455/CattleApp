package com.cattlelabs.cattleapp.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor(@ApplicationContext context: Context) {

    companion object {
        private const val PREF_NAME = "cattle_prefs"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_PHONE_NUMBER = "phone_number"
        private const val KEY_LOCATION = "location"
        private const val KEY_USER_LANGUAGE = "user_language"
        private const val DEFAULT_LANGUAGE = "en"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveSession(userId: String, userName: String, phoneNumber: String, location: String, userLanguage: String) {
        prefs.edit().apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_USER_NAME, userName)
            putString(KEY_PHONE_NUMBER, phoneNumber)
            putString(KEY_LOCATION, location)
            putString(KEY_USER_LANGUAGE, userLanguage)
            apply()
        }
    }

    fun saveUserLanguage(languageCode: String) {
        prefs.edit().putString(KEY_USER_LANGUAGE, languageCode).apply()
    }

    fun getUserLanguage(): String {
        return prefs.getString(KEY_USER_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun getUserId(): String? {
        return prefs.getString(KEY_USER_ID, null)
    }

    fun getUserName(): String? {
        return prefs.getString(KEY_USER_NAME, null)
    }

    fun getLocation(): String? {
        return prefs.getString(KEY_LOCATION, null)
    }

    fun getPhoneNumber(): String? {
        return prefs.getString(KEY_PHONE_NUMBER, null)
    }

    fun isLoggedIn(): Boolean {
        return !getUserId().isNullOrEmpty()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    // Optional: Get user data as a data class
    fun getUserSession(): UserSession? {
        val userId = getUserId()
        val userName = getUserName()
        val phoneNumber = getPhoneNumber()
        val location = getLocation()
        val language = getUserLanguage()

        return if (!userId.isNullOrEmpty()) {
            UserSession(userId, userName, phoneNumber, location, language)
        } else {
            null
        }
    }
}

// Optional: Data class for user session
data class UserSession(
    val userId: String,
    val userName: String?,
    val phoneNumber: String?,
    val location: String?,
    val language: String
)