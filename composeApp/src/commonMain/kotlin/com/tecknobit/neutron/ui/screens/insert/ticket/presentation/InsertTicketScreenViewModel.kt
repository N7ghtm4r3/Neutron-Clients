package com.tecknobit.neutron.ui.screens.insert.ticket.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.insert.shared.presentation.InsertScreenViewModel
import kotlinx.coroutines.launch

class InsertTicketScreenViewModel(
    val projectId: String,
    val ticketId: String?,
) : InsertScreenViewModel(
    revenueId = ticketId
){

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
                onSuccess = { onSuccessInsert() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}