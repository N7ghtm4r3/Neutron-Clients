package com.tecknobit.neutron

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tecknobit.equinoxcore.helpers.InputsValidator.Companion.DEFAULT_LANGUAGE
import com.tecknobit.neutron.MainActivity.Companion.appUpdateManager
import com.tecknobit.neutron.MainActivity.Companion.launcher
import moe.tlaster.precompose.navigation.BackHandler
import java.util.Locale

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
actual fun CheckForUpdatesAndLaunch() {
    // TODO: ENABLE THE AUTHORIZATION WITH FINGERPRINT ETC... THEN
    appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
        val isUpdateAvailable = info.updateAvailability() == UPDATE_AVAILABLE
        val isUpdateSupported = info.isImmediateUpdateAllowed
        if (isUpdateAvailable && isUpdateSupported) {
            appUpdateManager.startUpdateFlowForResult(
                info,
                launcher,
                AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build()
            )
        } else
            startSession()
    }.addOnFailureListener {
        startSession()
    }
}

/**
 * Method to set locale language for the application
 *
 */
actual fun setUserLanguage() {
    val tag = localUser.language ?: DEFAULT_LANGUAGE
    val locale = Locale(tag)
    Locale.setDefault(locale)
    val context = com.tecknobit.equinoxcompose.utilities.context.AppContext.get()
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
actual fun CloseApplicationOnNavBack() {
    val context = LocalContext.current as Activity
    BackHandler {
        context.finishAffinity()
    }
}