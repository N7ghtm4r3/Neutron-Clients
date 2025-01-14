package com.tecknobit.neutron

import androidx.compose.ui.window.ComposeUIViewController
import com.tecknobit.equinoxcompose.session.setUpSession

fun MainViewController() = ComposeUIViewController {
    setUpSession(
        hasBeenDisconnectedAction = {
            localUser.clear()
            navigator.navigate(SPLASHSCREEN)
        }
    )
    App()
}