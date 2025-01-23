package com.tecknobit.neutron.ui.screens.insert.ticket.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.neutron.PROJECT_REVENUE_SCREEN
import com.tecknobit.neutron.helpers.KReviewer
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.insert.shared.presentation.InsertScreenViewModel
import kotlinx.coroutines.launch

/**
 * The `InsertTicketScreenViewModel` class is the support class used to create a new ticket or
 * edit an exiting one
 *
 * @property projectId The identifier of the project where attach the ticket
 * @property ticketId The identifier of the ticket to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see InsertScreenViewModel
 */
class InsertTicketScreenViewModel(
    val projectId: String,
    val ticketId: String?,
) : InsertScreenViewModel(
    revenueId = ticketId
) {

    /**
     * Method to execute the correct request to insert the revenue
     */
    override fun insertRequest() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    insertTicket(
                        projectId = projectId,
                        ticketId = ticketId,
                        title = title.value,
                        value = keyboardState.parseAmount(),
                        description = description.value,
                        openingDate = insertionDate.value
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        navigator.navigate("$PROJECT_REVENUE_SCREEN/${projectId}")
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}