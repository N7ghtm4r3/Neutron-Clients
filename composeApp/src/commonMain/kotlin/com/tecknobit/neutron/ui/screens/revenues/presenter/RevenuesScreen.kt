@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.revenues.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.session.EquinoxScreen
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.ui.components.FirstPageProgressIndicator
import com.tecknobit.neutron.ui.components.NewPageProgressIndicator
import com.tecknobit.neutron.ui.icons.ReceiptLong
import com.tecknobit.neutron.ui.screens.revenues.components.RevenueItem
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import com.tecknobit.neutron.ui.theme.NeutronTheme
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.no_revenues_yet

class RevenuesScreen : EquinoxScreen<RevenuesScreenViewModel>(
    viewModel = RevenuesScreenViewModel()
) {

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        NeutronTheme {
            ManagedContent(
                viewModel = viewModel!!,
                content = {
                    Scaffold(
                        snackbarHost = { SnackbarHost(viewModel!!.snackbarHostState!!) }
                    ) {
                        RevenuesSection()
                    }
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun RevenuesSection() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResponsiveContent(
                onExpandedSizeClass = {
                    Revenues()
                },
                onMediumSizeClass = {
                    Revenues()
                },
                onCompactSizeClass = {
                    Revenues()
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun Revenues() {
        PaginatedLazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(
                    max = 1000.dp
                ),
            paginationState = viewModel!!.revenuesState,
            contentPadding = PaddingValues(
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            firstPageEmptyIndicator = {
                EmptyListUI(
                    icon = ReceiptLong,
                    subText = Res.string.no_revenues_yet,
                    textStyle = TextStyle(
                        fontFamily = bodyFontFamily
                    )
                )
            },
            newPageProgressIndicator = { NewPageProgressIndicator() }
        ) {
            items(
                items = viewModel!!.revenuesState.allItems!!,
                key = { revenue -> revenue.id }
            ) { revenue ->
                RevenueItem(
                    viewModel = viewModel!!,
                    revenue = revenue
                )
            }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    override fun CollectStates() {
    }

}