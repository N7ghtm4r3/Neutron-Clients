@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.revenues.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.neutron.ui.components.DeleteTicket
import com.tecknobit.neutron.ui.components.RevenueDescription
import com.tecknobit.neutron.ui.components.RevenueListItem
import com.tecknobit.neutron.ui.icons.ContractDelete
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.data.TicketRevenue
import com.tecknobit.neutroncore.CLOSED_TICKET_LABEL_COLOR
import com.tecknobit.neutroncore.PENDING_TICKET_LABEL_COLOR
import neutron.composeapp.generated.resources.closed_status
import neutron.composeapp.generated.resources.pending_status
import org.jetbrains.compose.resources.stringResource

@Composable
@NonRestartableComposable
fun TicketCard(
    viewModel: ProjectScreenViewModel,
    ticket: TicketRevenue,
    position: Int
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            if(position %2 == 0) {
                Card {
                    TicketRevenueContent(
                        viewModel = viewModel,
                        ticket = ticket,
                        containerColor = Color.Transparent
                    )
                }
            } else {
                TicketRevenueContent(
                    viewModel = viewModel,
                    ticket = ticket,
                    containerColor = Color.Transparent
                )
            }
        },
        onMediumSizeClass = {
            TicketRevenueContent(
                viewModel = viewModel,
                ticket = ticket
            )
            HorizontalDivider()
        },
        onCompactSizeClass = {
            TicketRevenueContent(
                viewModel = viewModel,
                ticket = ticket
            )
            HorizontalDivider()
        }
    )
}

@Composable
@NonRestartableComposable
private fun TicketRevenueContent(
    viewModel: ProjectScreenViewModel,
    ticket: TicketRevenue,
    containerColor: Color = ListItemDefaults.containerColor
) {
    Column {
        var expanded by remember { mutableStateOf(false) }
        RevenueListItem(
            revenue = ticket,
            labels = ticket.getTicketStatusLabel(),
            containerColor = containerColor,
            onEdit = {
                // TODO: NAV TO EDIT
            },
            deleteIcon = ContractDelete,
            actionButton = {
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if(expanded)
                            Icons.Default.ExpandLess
                        else
                            Icons.Default.ExpandMore,
                        contentDescription = null
                    )
                }
            },
            deleteAlertDialog = { delete ->
                DeleteTicket(
                    show = delete,
                    viewModel = viewModel,
                    ticket = ticket
                )
            }
        )
        RevenueDescription(
            expanded = expanded,
            revenue = ticket
        )
    }
}

@Composable
private fun TicketRevenue.getTicketStatusLabel() : List<RevenueLabel> {
    val pending = stringResource(neutron.composeapp.generated.resources.Res.string.pending_status)
    val closed = stringResource(neutron.composeapp.generated.resources.Res.string.closed_status)
    return remember {
        if(this.isPending()) {
            listOf(
                RevenueLabel(
                    text = pending,
                    color = PENDING_TICKET_LABEL_COLOR
                )
            )
        } else {
            listOf(
                RevenueLabel(
                    text = closed,
                    color = CLOSED_TICKET_LABEL_COLOR
                )
            )
        }
    }
}
