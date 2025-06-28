package com.tecknobit.neutron

import androidx.activity.compose.LocalActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.equinoxcore.utilities.AppContext
import com.tecknobit.equinoxcore.utilities.ContextActivityProvider
import com.tecknobit.neutron.MainActivity.Companion.appUpdateManager
import com.tecknobit.neutron.MainActivity.Companion.launcher
import com.tecknobit.neutron.helpers.BiometricPromptManager
import com.tecknobit.neutron.helpers.BiometricPromptManager.BiometricResult.AuthenticationNotSet
import com.tecknobit.neutron.helpers.BiometricPromptManager.BiometricResult.AuthenticationSuccess
import com.tecknobit.neutron.helpers.BiometricPromptManager.BiometricResult.FeatureUnavailable
import com.tecknobit.neutron.helpers.BiometricPromptManager.BiometricResult.HardwareUnavailable
import com.tecknobit.neutron.ui.theme.NeutronTheme
import moe.tlaster.precompose.navigation.BackHandler
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.enter_your_credentials_to_continue
import neutron.composeapp.generated.resources.login_required
import org.jetbrains.compose.resources.stringResource
import java.util.Locale

/**
 * `authWitBiometricParams` whether the biometric authentication must be effectuated because
 * it's the first launch of the application
 */
private var authWitBiometricParams: Boolean = true

/**
 * `biometricPromptManager` the manager used to authenticate with the bio params
 */
private val biometricPromptManager by lazy {
    BiometricPromptManager(ContextActivityProvider.getCurrentActivity() as AppCompatActivity)
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
actual fun CheckForUpdatesAndLaunch() {
    if(authWitBiometricParams) {
        val biometricResult by biometricPromptManager.promptResults.collectAsState(
            initial = null
        )
        LaunchedEffect(biometricResult) {
            if (biometricResult is AuthenticationNotSet)
                checkForUpdates()
        }
        if(biometricResult == null) {
            biometricPromptManager.showBiometricPrompt(
                title = stringResource(Res.string.login_required),
                description = stringResource(Res.string.enter_your_credentials_to_continue)
            )
        }
        biometricResult?.let { result ->
            when(result) {
                AuthenticationSuccess, AuthenticationNotSet, HardwareUnavailable,
                FeatureUnavailable -> checkForUpdates()
                else -> {
                    NeutronTheme {
                        ErrorUI(
                            containerModifier = Modifier
                                .fillMaxSize(),
                            retryAction = { CheckForUpdatesAndLaunch() }
                        )
                    }
                }
            }
        }
    } else
        checkForUpdates()
}

/**
 * Method to check whether are available any updates for the current application version.
 * If available and allowed to install, after the installation enter in the application
 */
private fun checkForUpdates() {
    authWitBiometricParams = false
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
    val locale = Locale(localUser.language)
    Locale.setDefault(locale)
    val context = AppContext.get()
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
}

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
actual fun CloseApplicationOnNavBack() {
    val context = LocalActivity.current!!
    BackHandler {
        context.finishAffinity()
    }
}