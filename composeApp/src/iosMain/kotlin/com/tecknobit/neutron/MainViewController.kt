package com.tecknobit.neutron

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.equinoxcompose.session.setUpSession

/**
 * Method to start the of`Neutron** iOs app
 *
 */
fun MainViewController() {
    AmetistaEngine.intake()
    ComposeUIViewController {
        setUpSession(
            hasBeenDisconnectedAction = {
                localUser.clear()
                navigator.navigate(SPLASHSCREEN)
            }
        )
        App()
    }
}