package com.tecknobit.neutron.ui.screens.project.presentation

import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.neutron.helpers.PeriodFiltererViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.TicketRevenue
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlin.random.Random

class ProjectScreenViewModel(
    projectId: String
) : PeriodFiltererViewModel() {

    private val _project = MutableStateFlow<ProjectRevenue?>(
        value = null
    )
    val project: StateFlow<ProjectRevenue?> = _project

    private var retrievePendingTickets: Boolean = true

    private var retrieveClosedTickets: Boolean = true

    fun retrieveProject() {
        // TODO: MAKE THE REQUEST THEN
        _project.value = ProjectRevenue(
            id = "prova",
            title = "Prova",
            value = 0.0,
            revenueDate = Clock.System.now().toEpochMilliseconds(),
            initialRevenue = Revenue.RevenueImpl(
                id = "g",
                title = "ga",
                revenueDate = Clock.System.now().toEpochMilliseconds(),
                value = 1000.0
            )
        )
    }

    val ticketsState = PaginationState<Int, TicketRevenue>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadTickets(
                page = page
            )
        }
    )

    private fun loadTickets(
        page: Int
    ) {
        // TODO: MAKE THE REQUEST THEN
        // TODO: APPLY THE FILTERS
        val tickets = listOf<TicketRevenue>()
        // TODO: USE THE REAL VALUES
        ticketsState.appendPage(
            items = tickets,
            nextPageKey = 1, // TODO: USE THE ONE FROM THE PaginationResponse
            isLastPage = Random.nextBoolean() // TODO: USE THE ONE FROM THE PaginationResponse
        )
    }

    fun applyRetrievePendingTickets(
        retrieve: Boolean
    ) {
        retrievePendingTickets = retrieve
    }

    fun applyRetrieveClosedTickets(
        retrieve: Boolean
    ) {
        retrieveClosedTickets = retrieve
    }

    override fun refreshData() {
        ticketsState.refresh()
    }

}