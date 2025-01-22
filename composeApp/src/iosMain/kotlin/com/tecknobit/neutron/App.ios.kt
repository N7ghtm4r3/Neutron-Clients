@file:OptIn(ExperimentalForeignApi::class)

package com.tecknobit.neutron

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicyDeviceOwnerAuthenticationWithBiometrics

/**
 * `context` the context used to check the availability about the bio-auth and then evaluate the policy
 * to validate the authentication with the biometrics
 */
private val context = LAContext()

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    authenticateWithBiometrics()
}

/**
 * Method to execute the bio-authentication if available and then enter in the application
 *
 */
private fun authenticateWithBiometrics() {
    if (context.canEvaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, null)) {
        context.evaluatePolicy(LAPolicyDeviceOwnerAuthenticationWithBiometrics, "") { success, _ ->
            if(success)
                startSession()
            else
                authenticateWithBiometrics()
        }
    } else
        startSession()
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
@Composable
@NonRestartableComposable
actual fun CloseApplicationOnNavBack() {
}