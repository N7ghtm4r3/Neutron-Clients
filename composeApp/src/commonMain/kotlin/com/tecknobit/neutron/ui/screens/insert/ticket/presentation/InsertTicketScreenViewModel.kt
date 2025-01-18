package com.tecknobit.neutron.ui.screens.insert.ticket.presentation

import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.screens.insert.shared.presentation.InsertScreenViewModel

class InsertTicketScreenViewModel(
    ticketId: String?
) : InsertScreenViewModel(
    revenueId = ticketId
){

    override fun insertRequest() {
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
    }

}