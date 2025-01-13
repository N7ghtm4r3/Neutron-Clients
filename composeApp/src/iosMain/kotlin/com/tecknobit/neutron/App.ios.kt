package com.tecknobit.neutron

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import kotlinx.coroutines.delay
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    LaunchedEffect(Unit) {
        delay(1000)
        startSession()
    }
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    val locale = NSLocale(localeIdentifier = localUser.language ?: DEFAULT_LANGUAGE)
    NSUserDefaults.standardUserDefaults.setObject(locale, forKey = "AppleLanguages")
    NSUserDefaults.standardUserDefaults.synchronize()
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
}