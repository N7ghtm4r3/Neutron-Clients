package com.tecknobit.neutron.ui.screens.insert.ticket.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tecknobit.neutron.ui.components.Step
import com.tecknobit.neutron.ui.screens.insert.shared.presenter.InsertScreen
import com.tecknobit.neutron.ui.screens.insert.ticket.presentation.InsertTicketScreenViewModel
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_ticket
import neutron.composeapp.generated.resources.edit_ticket

class InsertTicketScreen(
    projectId: String,
    ticketId: String?,
) : InsertScreen<InsertTicketScreenViewModel>(
    revenueId = ticketId,
    addingTitle = Res.string.add_ticket,
    editingTitle = Res.string.edit_ticket,
    viewModel = InsertTicketScreenViewModel(
        projectId = projectId,
        ticketId = ticketId
    )
) {

    @Composable
    override fun getInsertionSteps(): Array<out Step> {
        return remember {
            arrayOf(
                titleStep,
                descriptionStep,
                insertionDateStep
            )
        }
    }

}