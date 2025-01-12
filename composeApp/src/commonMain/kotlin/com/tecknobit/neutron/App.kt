package com.tecknobit.neutron

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import com.tecknobit.neutron.ui.helpers.NeutronLocalUser
import com.tecknobit.neutron.ui.helpers.NeutronRequester
import com.tecknobit.neutron.ui.screens.SplashScreen
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.lilitaone
import neutron.composeapp.generated.resources.roboto
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 *`bodyFontFamily` the Neutron's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 *`displayFontFamily` the Neutron's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 * `navigator` -> the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: Navigator

/**
 *`imageLoader` the image loader used by coil library to load the image and by-passing the https self-signed certificates
 */
/*lateinit var imageLoader: ImageLoader*/

/**
 *`requester` the instance to manage the requests with the backend
 */
lateinit var requester: NeutronRequester

/**
 *`localUser` the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = NeutronLocalUser()

/**
 * `SPLASHSCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.splashscreen.Splashscreen]
 */
const val SPLASHSCREEN = "Splashscreen"

/**
 * `AUTH_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.auth.presenter.AuthScreen]
 */
const val AUTH_SCREEN = "AuthScreen"

/**
 * `REVENUES_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.auth.presenter.AuthScreen]
 */
const val REVENUES_SCREEN = "RevenuesScreen"

@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.roboto))
    displayFontFamily = FontFamily(Font(Res.font.lilitaone))
    PreComposeApp {
        navigator = rememberNavigator()
        NavHost(
            navigator = navigator,
            initialRoute = SPLASHSCREEN
        ) {
            scene(
                route = SPLASHSCREEN
            ) {
                SplashScreen().ShowContent()
            }
        }
    }
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = NeutronRequester(
        host = localUser.hostAddress ?: "",
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    val route = if (localUser.userId == null)
        AUTH_SCREEN
    else
        REVENUES_SCREEN
    setUserLanguage()
    navigator.navigate(route)
}

/**
 * Method to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()