@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * The `RevenuesContainerScreen` interface allows to give the basic behavior to those screens which
 * contains a list of revenues
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface RevenuesContainerScreen {

    companion object {

        /**
         *`HIDE_BALANCE` constant value used to hide balances values
         */
        const val HIDE_BALANCE = "****"

    }

    /**
     *`hideBalances` state used to manage the visibilities of the balances
     */
    var hideBalances: State<Boolean>

    /**
     * Custom floating action buttons responsively adapted
     */
    @Composable
    fun FabButton() {
        ResponsiveContent(
            onExpandedSizeClass = {
                ExtendedFloatingActionButton(
                    onClick = { navToInsert() }
                ) {
                    Row (
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                            contentDescription = null
                        )
                        Text(
                            text = stringResource(extendedFabText())
                        )
                    }
                }
            },
            onMediumSizeClass = { CompactFabButton() },
            onCompactSizeClass = { CompactFabButton() }
        )
    }

    /**
     * Method to get the extended floating action button text
     *
     * @return the text to for the extended floating action as [StringResource]
     */
    fun extendedFabText() : StringResource

    /**
     * The floating action button displayed on a medium and compact size class devices
     */
    @Composable
    @NonRestartableComposable
    fun CompactFabButton() {
        FloatingActionButton(
            onClick = { navToInsert() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                contentDescription = null
            )
        }
    }

    /**
     * Method to navigate to the related [com.tecknobit.neutron.ui.screens.insert.shared.presenter.InsertScreen]
     */
    fun navToInsert()

    /**
     * The header section of the screen
     */
    @Composable
    @ScreenSection
    fun Header()

}