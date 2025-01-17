package com.tecknobit.neutron

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.neutron.helpers.NeutronLocalUser
import com.tecknobit.neutron.helpers.NeutronRequester
import com.tecknobit.neutron.helpers.customHttpClient
import com.tecknobit.neutron.ui.screens.SplashScreen
import com.tecknobit.neutron.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.neutron.ui.screens.insert.presenter.InsertRevenueScreen
import com.tecknobit.neutron.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.neutron.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.neutron.ui.screens.revenues.presenter.RevenuesScreen
import com.tecknobit.neutroncore.REVENUE_IDENTIFIER_KEY
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
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
lateinit var imageLoader: ImageLoader

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

/**
 * `PROFILE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.profile.presenter.ProfileScreen]
 */
const val PROFILE_SCREEN = "ProfileScreen"

/**
 * `INSERT_REVENUE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.insert.presenter.InsertRevenueScreen]
 */
const val INSERT_REVENUE_SCREEN = "InsertRevenueScreen"

/**
 * `PROJECT_REVENUE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.project.presenter.ProjectScreen]
 */
const val PROJECT_REVENUE_SCREEN = "ProjectRevenueScreen"

val MAX_CONTAINER_WIDTH = 1280.dp

@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.roboto))
    displayFontFamily = FontFamily(Font(Res.font.lilitaone))
    imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                KtorNetworkFetcherFactory(
                    httpClient = customHttpClient()
                )
            )
        }
        .addLastModifiedToFileCacheKey(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
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
            scene(
                route = AUTH_SCREEN
            ) {
                AuthScreen().ShowContent()
            }
            scene(
                route = REVENUES_SCREEN
            ) {
                RevenuesScreen().ShowContent()
            }
            scene(
                route = PROFILE_SCREEN
            ) {
                // TODO: TO REMOVE THIS 
                requester = NeutronRequester(
                    host = ""
                )
                // TODO: TO REMOVE ABOVE
                ProfileScreen().ShowContent()
            }
            scene(
                route = "$INSERT_REVENUE_SCREEN/{$REVENUE_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val revenueId : String? = backstackEntry.path(REVENUE_IDENTIFIER_KEY)
                InsertRevenueScreen(
                    revenueId = revenueId
                ).ShowContent()
            }
            scene(
                route = "$PROJECT_REVENUE_SCREEN/{$REVENUE_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
                val revenueId : String = backstackEntry.path(REVENUE_IDENTIFIER_KEY)!!
                ProjectScreen(
                    projectId = revenueId
                ).ShowContent()
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
    val route = if (localUser.isAuthenticated)
        REVENUES_SCREEN
    else
        AUTH_SCREEN
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