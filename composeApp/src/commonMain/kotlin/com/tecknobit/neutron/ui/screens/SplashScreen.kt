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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.session.EquinoxScreen
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.neutron.CheckForUpdatesAndLaunch
import com.tecknobit.neutron.CloseApplicationOnNavBack
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.displayFontFamily
import com.tecknobit.neutron.ui.theme.NeutronTheme
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

/**
 * The [SplashScreen] class is used to retrieve and load the session data and enter the application's workflow
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
class SplashScreen : EquinoxScreen<EquinoxViewModel>() {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        CloseApplicationOnNavBack()
        NeutronTheme {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column (
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
                Column (
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
        }
        CheckForUpdatesAndLaunch()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}