package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.EXPANDED_CONTAINER
import com.tecknobit.neutron.ui.components.FirstPageProgressIndicator
import com.tecknobit.neutron.ui.components.NewPageProgressIndicator
import com.tecknobit.neutron.ui.components.NoRevenues
import com.tecknobit.neutron.ui.screens.revenues.presentation.RevenuesScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.no_revenues_yet

/**
 * The current revenues owned by the user
 *
 * @param viewModel The support viewmodel of the screen
 */
@Composable
fun Revenues(
    viewModel: RevenuesScreenViewModel,
) {
    PaginatedLazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .widthIn(
                max = EXPANDED_CONTAINER
            )
            .animateContentSize()
            .navigationBarsPadding(),
        paginationState = viewModel.revenuesState,
        contentPadding = PaddingValues(
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        firstPageEmptyIndicator = {
            NoRevenues(
                titleRes = Res.string.no_revenues_yet
            )
        },
        newPageProgressIndicator = { NewPageProgressIndicator() }
    ) {
        itemsIndexed(
            items = viewModel.revenuesState.allItems!!,
            key = { _, revenue -> revenue.id }
        ) { index, revenue ->
            RevenueCard(
                viewModel = viewModel,
                revenue = revenue,
                position = index
            )
        }
    }
}