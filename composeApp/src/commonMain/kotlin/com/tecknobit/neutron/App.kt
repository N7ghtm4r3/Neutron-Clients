@file:OptIn(ExperimentalComposeApi::class, ExperimentalStdlibApi::class)

package com.tecknobit.neutron

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.ametista.AmetistaConfig
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.ametistaengine.AmetistaEngine.Companion.FILES_AMETISTA_CONFIG_PATHNAME
import com.tecknobit.biometrik.rememberBiometrikState
import com.tecknobit.equinoxcompose.session.screens.equinoxScreen
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.neutron.helpers.NeutronLocalUser
import com.tecknobit.neutron.helpers.NeutronRequester
import com.tecknobit.neutron.helpers.customHttpClient
import com.tecknobit.neutron.ui.screens.SplashScreen
import com.tecknobit.neutron.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.neutron.ui.screens.insert.revenue.presenter.InsertRevenueScreen
import com.tecknobit.neutron.ui.screens.insert.ticket.presenter.InsertTicketScreen
import com.tecknobit.neutron.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.neutron.ui.screens.project.presenter.ProjectScreen
import com.tecknobit.neutron.ui.screens.revenues.presenter.RevenuesScreen
import com.tecknobit.neutron.ui.theme.NeutronTheme
import com.tecknobit.neutroncore.REVENUE_IDENTIFIER_KEY
import com.tecknobit.neutroncore.TICKET_IDENTIFIER_KEY
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.lilitaone
import neutron.composeapp.generated.resources.roboto
import org.jetbrains.compose.resources.Font

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
lateinit var navigator: NavHostController

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
 * `INSERT_REVENUE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.insert.revenue.presenter.InsertRevenueScreen]
 */
const val INSERT_REVENUE_SCREEN = "InsertRevenueScreen"

/**
 * `INSERT_TICKET_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.insert.ticket.presenter.InsertTicketScreen]
 */
const val INSERT_TICKET_SCREEN = "InsertTicketScreen"

/**
 * `PROJECT_REVENUE_SCREEN` route to navigate to the [com.tecknobit.neutron.ui.screens.project.presenter.ProjectScreen]
 */
const val PROJECT_REVENUE_SCREEN = "ProjectRevenueScreen"

/**
 * Common entry point of The `Neutron** application
 */
@Composable
fun App() {
    val biometrikState = rememberBiometrikState()
    // InitAmetista()
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
    navigator = rememberNavController()
    // TODO: TO USE THESE UNIQUE THEME
    // NeutronTheme {
        NavHost(
            navController = navigator,
            startDestination = SPLASHSCREEN
        ) {
            composable(
                route = SPLASHSCREEN
            ) {
                // TODO: TO USE THE UNIQUE THEME
                NeutronTheme {
                    val splashScreen = equinoxScreen {
                        SplashScreen(
                            biometrikState = biometrikState
                        )
                    }
                    splashScreen.ShowContent()
                }
            }
            composable(
                route = AUTH_SCREEN
            ) {
                // TODO: TO USE THE UNIQUE THEME
                NeutronTheme {
                    val authScreen = equinoxScreen { AuthScreen() }
                    authScreen.ShowContent()
                }
            }
            composable(
                route = REVENUES_SCREEN
            ) {
                // TODO: TO USE THE UNIQUE THEME
                NeutronTheme {
                    val revenuesScreen = equinoxScreen { RevenuesScreen() }
                    revenuesScreen.ShowContent()
                }
            }
            composable(
                route = PROFILE_SCREEN
            ) {
                // TODO: TO USE THE UNIQUE THEME
                NeutronTheme {
                    val profileScreen = equinoxScreen { ProfileScreen() }
                    profileScreen.ShowContent()
                }
            }
            composable(
                route = INSERT_REVENUE_SCREEN
            ) {
                // TODO: TO USE THE UNIQUE THEME
                NeutronTheme {
                    val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                    val revenueId: String? = savedStateHandle[REVENUE_IDENTIFIER_KEY]
                    val insertRevenueScreen = equinoxScreen {
                        InsertRevenueScreen(
                            revenueId = revenueId
                        )
                    }
                    insertRevenueScreen.ShowContent()
                    savedStateHandle.remove<String>(REVENUE_IDENTIFIER_KEY)
                }
            }
            composable(
                route = PROJECT_REVENUE_SCREEN
            ) {
                // TODO: TO USE THE UNIQUE THEME
                NeutronTheme {
                    val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                    val projectId: String? = savedStateHandle[REVENUE_IDENTIFIER_KEY]
                    projectId?.let {
                        val projectScreen = equinoxScreen {
                            ProjectScreen(
                                projectId = projectId
                            )
                        }
                        projectScreen.ShowContent()
                    }
                }
            }
            composable(
                route = INSERT_TICKET_SCREEN
            ) {
                // TODO: TO USE THE UNIQUE THEME
                NeutronTheme {
                    val savedStateHandle = navigator.previousBackStackEntry?.savedStateHandle!!
                    val projectId: String = savedStateHandle[REVENUE_IDENTIFIER_KEY]!!
                    val ticketId: String? = savedStateHandle[TICKET_IDENTIFIER_KEY]
                    val insertTicketScreen = equinoxScreen {
                        InsertTicketScreen(
                            projectId = projectId,
                            ticketId = ticketId
                        )
                    }
                    insertTicketScreen.ShowContent()
                    savedStateHandle.remove<String>(TICKET_IDENTIFIER_KEY)
                }
            }
        }
    // }
    SessionFlowState.invokeOnUserDisconnected {
        localUser.clear()
        navigator.navigate(SPLASHSCREEN)
    }
}

/**
 * Method used to initialize the Ametista system
 */
// TODO: REIMPLEMENT WHEN NECESSARY
@Composable
@NonRestartableComposable
private fun InitAmetista() {
    LaunchedEffect(Unit) {
        val ametistaEngine = AmetistaEngine.ametistaEngine
        ametistaEngine.fireUp(
            configData = Res.readBytes(FILES_AMETISTA_CONFIG_PATHNAME),
            host = AmetistaConfig.HOST,
            serverSecret = AmetistaConfig.SERVER_SECRET!!,
            applicationId = AmetistaConfig.APPLICATION_IDENTIFIER!!,
            bypassSslValidation = AmetistaConfig.BYPASS_SSL_VALIDATION,
            debugMode = false
        )
    }
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = NeutronRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    val route = if (localUser.isAuthenticated) {
        MainScope().launch {
            requester.sendRequest(
                request = { getDynamicAccountData() },
                onSuccess = { response ->
                    localUser.updateDynamicAccountData(
                        dynamicData = response.toResponseData()
                    )
                    setUserLanguage()
                },
                onFailure = { setUserLanguage() }
            )
        }
        REVENUES_SCREEN
    } else
        AUTH_SCREEN
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
expect fun CloseApplicationOnNavBack()