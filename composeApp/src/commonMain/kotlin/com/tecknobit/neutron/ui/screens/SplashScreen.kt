@file:OptIn(ExperimentalComposeApi::class)

package com.tecknobit.neutron.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.biometrik.BiometrikAuthenticator
import com.tecknobit.biometrik.BiometrikState
import com.tecknobit.equinoxcompose.components.ErrorUI
import com.tecknobit.equinoxcompose.components.RetryButton
import com.tecknobit.equinoxcompose.session.screens.EquinoxNoModelScreen
import com.tecknobit.neutron.CheckForUpdatesAndLaunch
import com.tecknobit.neutron.CloseApplicationOnNavBack
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.app_name
import neutron.composeapp.generated.resources.enter_your_credentials_to_continue
import neutron.composeapp.generated.resources.login_required
import org.jetbrains.compose.resources.stringResource

/**
 * The [SplashScreen] class is used to retrieve and load the session data and enter the application's workflow
 *
 * @param biometrikState The state used to handle the bio authentication
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxNoModelScreen
 */
class SplashScreen(
    private val biometrikState: BiometrikState,
) : EquinoxNoModelScreen() {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = stringResource(Res.string.app_name),
                    color = Color.White,
                    fontFamily = displayFontFamily,
                    fontSize = 45.sp
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(
                        bottom = 16.dp
                    )
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "by Tecknobit",
                    fontFamily = bodyFontFamily,
                    color = Color.White
                )
            }
        }
        BiometrikAuthenticator(
            state = biometrikState,
            appName = stringResource(Res.string.app_name),
            title = stringResource(Res.string.login_required),
            reason = stringResource(Res.string.enter_your_credentials_to_continue),
            onSuccess = { CheckForUpdatesAndLaunch() },
            onFailure = {
                ErrorUI(
                    containerModifier = Modifier
                        .fillMaxSize(),
                    retryContent = {
                        RetryButton(
                            onRetry = { biometrikState.reAuth() }
                        )
                    }
                )
            }
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}