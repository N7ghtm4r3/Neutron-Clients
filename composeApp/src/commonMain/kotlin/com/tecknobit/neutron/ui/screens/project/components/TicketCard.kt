@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.neutron.ui.screens.project.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.neutron.INSERT_TICKET_SCREEN
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.components.DeleteTicket
import com.tecknobit.neutron.ui.components.RevenueDescription
import com.tecknobit.neutron.ui.components.RevenueListItem
import com.tecknobit.neutron.ui.components.TicketInfo
import com.tecknobit.neutron.ui.icons.ContractDelete
import com.tecknobit.neutron.ui.screens.project.presentation.ProjectScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.components.RevenueLabels
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.data.TicketRevenue
import com.tecknobit.neutroncore.CLOSED_TICKET_LABEL_COLOR
import com.tecknobit.neutroncore.PENDING_TICKET_LABEL_COLOR
import kotlinx.datetime.Clock
import neutron.composeapp.generated.resources.closed_status
import neutron.composeapp.generated.resources.pending_status
import org.jetbrains.compose.resources.stringResource

/**
 * Custom card used to display a ticket details
 *
 * @param viewModel The support viewmodel for the screen
 * @param project The project where the ticket is attached
 * @param ticket The ticket to display
 * @param position The position occupied by the ticket in the list
 */
@Composable
fun TicketCard(
    viewModel: ProjectScreenViewModel,
    project: ProjectRevenue,
    ticket: TicketRevenue,
    position: Int,
) {
    ResponsiveContent(
        onExpandedSizeClass = {
            if(position %2 == 0) {
                Card {
                    TicketRevenueContent(
                        viewModel = viewModel,
                        project = project,
                        ticket = ticket,
                        containerColor = Color.Transparent
                    )
                }
            } else {
                TicketRevenueContent(
                    viewModel = viewModel,
                    project = project,
                    ticket = ticket,
                    containerColor = Color.Transparent
                )
            }
        },
        onMediumSizeClass = {
            TicketRevenueContent(
                viewModel = viewModel,
                project = project,
                ticket = ticket
            )
            HorizontalDivider()
        },
        onCompactSizeClass = {
            TicketRevenueContent(
                viewModel = viewModel,
                project = project,
                ticket = ticket
            )
            HorizontalDivider()
        }
    )
}

/**
 * Custom card used to display a ticket details
 *
 * @param viewModel The support viewmodel for the screen
 * @param project The project where the ticket is attached
 * @param ticket The ticket to display
 * @param containerColor The color to use for the container
 */
@Composable
private fun TicketRevenueContent(
    viewModel: ProjectScreenViewModel,
    project: ProjectRevenue,
    ticket: TicketRevenue,
    containerColor: Color = ListItemDefaults.containerColor,
) {
    Column {
        var expanded by remember { mutableStateOf(false) }
        RevenueListItem(
            viewModel = viewModel,
            revenue = ticket,
            labels = ticket.getTicketStatusLabel(),
            containerColor = containerColor,
            allowEdit = ticket.isPending(),
            onEdit = {
                navigator.navigate("$INSERT_TICKET_SCREEN/${project.id}/${ticket.id}")
            },
            overline = { ticketLabel ->
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RevenueLabels(
                        labels = ticketLabel
                    )
                    AnimatedVisibility(
                        visible = ticket.isPending() &&
                                Clock.System.now().toEpochMilliseconds() >= ticket.revenueDate
                    ) {
                        IconButton(
                            modifier = Modifier
                                .size(28.dp),
                            onClick = {
                                viewModel.closeTicket(
                                    ticket = ticket
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = CLOSED_TICKET_LABEL_COLOR.toColor()
                            )
                        }
                    }
                }
            },
            info = {
                TicketInfo(
                    viewModel = viewModel,
                    ticket = ticket
                )
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

/**
 * Method to get the related labels based on the current status of a ticket
 *
 * @return the ticket status labels as [List] of [RevenueLabel]
 */
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
