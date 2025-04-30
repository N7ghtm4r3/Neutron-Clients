package com.tecknobit.neutron

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.tecknobit.equinoxcompose.session.setUpSession
import kotlinx.browser.document

/**
 * Method to start the of`Neutron** webapp
 *
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    //TODO: To integrate after AmetistaEngine.intake()
    ComposeViewport(document.body!!) {
        setUpSession(
            hasBeenDisconnectedAction = {
                localUser.clear()
                navigator.navigate(SPLASHSCREEN)
            }
        )
        App()
    }
}