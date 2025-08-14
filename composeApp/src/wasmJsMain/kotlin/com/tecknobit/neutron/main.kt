package com.tecknobit.neutron

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

/**
 * Method to start the of`Neutron** webapp
 *
 */
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // AmetistaEngine.intake()
    ComposeViewport(document.body!!) {
        App()
    }
}