package com.tecknobit.neutron

import androidx.compose.ui.window.ComposeUIViewController

/**
 * Method to start the of `Neutron` iOs app
 *
 */
fun MainViewController() {
    // AmetistaEngine.intake()
    ComposeUIViewController {
        App()
    }
}