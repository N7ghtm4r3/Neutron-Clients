package com.tecknobit.neutron.ui.screens.insert.ticket.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.tecknobit.equinoxcompose.session.EquinoxScreen
import com.tecknobit.neutron.ui.components.Step
import com.tecknobit.neutron.ui.screens.insert.shared.presenter.InsertScreen
import com.tecknobit.neutron.ui.screens.insert.ticket.presentation.InsertTicketScreenViewModel
import com.tecknobit.neutron.ui.screens.shared.presenters.NeutronScreen
import neutron.composeapp.generated.resources.Res
import neutron.composeapp.generated.resources.add_ticket
import neutron.composeapp.generated.resources.edit_ticket

/**
 * The [InsertTicketScreen] is used to allow the user to add or to edit a ticket
 *
 * @param projectId The identifier of the project where attach the ticket
 * @param ticketId The identifier of the ticket to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see NeutronScreen
 * @see InsertScreen
 */
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

    /**
     * Method to get the custom insertion steps for the [FormSection]
     *
     * @return the custom insertion steps as [Array] of out [Step]
     */
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