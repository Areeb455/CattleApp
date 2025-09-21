package com.cattlelabs.cattleapp

import android.app.Application
import com.cattlelabs.cattleapp.data.Prefs
import com.cattlelabs.cattleapp.ui.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CattleApp : Application() {

    @Inject
    lateinit var prefs: Prefs

    override fun onCreate() {
        super.onCreate()
        // Get the saved language code from SharedPreferences
        val languageCode = prefs.getUserLanguage()
        // Set the app's language to the saved preference
        LocaleHelper.updateLocale(this, languageCode)
    }
}