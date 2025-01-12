package com.tecknobit.neutron

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@NonRestartableComposable
@Composable
actual fun CloseApplicationOnNavBack() {
}