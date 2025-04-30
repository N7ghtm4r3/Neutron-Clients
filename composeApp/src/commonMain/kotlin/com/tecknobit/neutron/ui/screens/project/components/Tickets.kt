package com.tecknobit.neutron.ui.screens.project.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.neutron.bodyFontFamily
import com.tecknobit.neutron.ui.components.FirstPageProgressIndicator
import com.tecknobit.neutron.ui.components.NewPageProgressIndicator
import com.tecknobit.neutron.ui.icons.ReceiptLong
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.shared.data.ProjectRevenue
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.no_revenues_yet

/**
 * The tickets attached to the [project]
 *
 * @param viewModel The support viewmodel of the screen
 * @param project The project where the tickets are attached
 */
@Composable
fun Tickets(
    viewModel: ProjectScreenViewModel,
    project: ProjectRevenue,
) {
    PaginatedLazyColumn(
        paginationState = viewModel.ticketsState,
        contentPadding = PaddingValues(
            bottom = 16.dp
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
        itemsIndexed(
            items = viewModel.ticketsState.allItems!!,
            key = { _, revenue -> revenue.id }
        ) { index, ticket ->
            TicketCard(
                viewModel = viewModel,
                project = project,
                ticket = ticket,
                position = index
            )
        }
    }
}