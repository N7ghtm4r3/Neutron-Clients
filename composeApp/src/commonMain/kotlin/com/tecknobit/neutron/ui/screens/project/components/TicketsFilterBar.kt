package com.tecknobit.neutron.ui.screens.project.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.neutron.ui.components.CategoryChip
import com.tecknobit.neutron.ui.components.PeriodFilterChip
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.closed
import neutron.composeapp.generated.resources.pending

@Composable
@NonRestartableComposable
fun TicketsFilterBar(
    viewModel: ProjectScreenViewModel
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.dp
            )
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        PeriodFilterChip(
            viewModel = viewModel
        )
        CategoryChip(
            category = Res.string.pending,
            onClick = { retrieve ->
                viewModel.applyRetrievePendingTickets(
                    retrieve = retrieve
                )
            }
        )
        CategoryChip(
            category = Res.string.closed,
            onClick = { retrieve ->
                viewModel.applyRetrieveClosedTickets(
                    retrieve = retrieve
                )
            }
        )
    }
}

