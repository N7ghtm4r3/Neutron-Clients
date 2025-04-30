package com.tecknobit.neutron.ui.screens.project.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.neutron.ui.components.FirstPageProgressIndicator
import com.tecknobit.neutron.ui.components.NewPageProgressIndicator
import com.tecknobit.neutron.ui.components.NoRevenues
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.shared.data.ProjectRevenue
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.no_tickets_yet

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
        modifier = Modifier
            .animateContentSize(),
        paginationState = viewModel.ticketsState,
        contentPadding = PaddingValues(
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        firstPageEmptyIndicator = {
            NoRevenues(
                titleRes = Res.string.no_tickets_yet
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