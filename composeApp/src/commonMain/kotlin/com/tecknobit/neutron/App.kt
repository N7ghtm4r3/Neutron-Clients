package com.tecknobit.neutron

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import com.tecknobit.neutron.ui.helpers.NeutronLocalUser
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * **bodyFontFamily** -> the Neutron's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * **displayFontFamily** -> the Neutron's font family
 */
lateinit var displayFontFamily: FontFamily

val localUser = NeutronLocalUser()

@Composable
@Preview
fun App() {

}