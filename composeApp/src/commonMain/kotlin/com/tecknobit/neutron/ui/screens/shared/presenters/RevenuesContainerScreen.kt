package com.tecknobit.neutron.ui.screens.shared.presenters

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StickyNote2
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.tecknobit.equinoxcompose.annotations.ScreenSection
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
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
        ExtendedFloatingActionButton(
            expanded = responsiveAssignment(
                onExpandedSizeClass = { true },
                onMediumSizeClass = { false },
                onCompactSizeClass = { false }
            ),
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.StickyNote2,
                    contentDescription = null
                )
            },
            text = {
                Text(
                    text = stringResource(extendedFabText())
                )
            },
            onClick = { navToInsert() }
        )
    }

    /**
     * Method to get the extended floating action button text
     *
     * @return the text to for the extended floating action as [StringResource]
     */
    fun extendedFabText() : StringResource

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