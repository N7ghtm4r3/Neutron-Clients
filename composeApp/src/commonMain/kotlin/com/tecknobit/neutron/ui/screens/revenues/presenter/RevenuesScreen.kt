package com.tecknobit.neutron.ui.screens.revenues.presenter

import androidx.compose.runtime.Composable
import com.tecknobit.equinoxcompose.session.EquinoxScreen
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel

class RevenuesScreen : EquinoxScreen<RevenuesScreenViewModel>(
    viewModel = RevenuesScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}