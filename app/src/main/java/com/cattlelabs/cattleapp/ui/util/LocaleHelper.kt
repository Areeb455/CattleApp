package com.cattlelabs.cattleapp.ui.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {
    fun updateLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        val localeList = LocaleListCompat.create(locale)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}