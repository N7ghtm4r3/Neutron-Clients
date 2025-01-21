package com.tecknobit.neutron.ui.screens.project.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseContent
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.revenues.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.TicketRevenue
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class ProjectScreenViewModel(
    val projectId: String,
) : RevenueRelatedScreenViewModel() {

    private val _project = MutableStateFlow<ProjectRevenue?>(
        value = null
    )
    val project: StateFlow<ProjectRevenue?> = _project

    private val _balance = MutableStateFlow(
        value = 0.0
    )
    val balance: StateFlow<Double> = _balance

    private var retrievePendingTickets: Boolean = true

    private var retrieveClosedTickets: Boolean = true

    fun retrieveProject() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getProjectRevenue(
                        projectId = projectId
                    )
                },
                onSuccess = {
                    _project.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = {
                    showSnackbarMessage(it)
                }
            )
        }
    }

    fun getProjectBalance() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getProjectBalance(
                        projectId = projectId,
                        period = _revenuePeriod.value,
                        retrieveClosedTickets = retrieveClosedTickets
                    )
                },
                onSuccess = {
                    _balance.value = it.toResponseContent().toDouble()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
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
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getTickets(
                        projectId = projectId,
                        page = page,
                        period = _revenuePeriod.value,
                        retrievePendingTickets = retrievePendingTickets,
                        retrieveClosedTickets = retrieveClosedTickets
                    )
                },
                serializer = TicketRevenue.serializer(),
                onSuccess = { paginatedResponse ->
                    setServerOfflineValue(false)
                    ticketsState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                    getProjectBalance()
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = {
                    setServerOfflineValue(true)
                }
            )
        }
    }

    fun applyRetrievePendingTickets(
        retrieve: Boolean
    ) {
        retrievePendingTickets = retrieve
        refreshData()
    }

    fun applyRetrieveClosedTickets(
        retrieve: Boolean
    ) {
        retrieveClosedTickets = retrieve
        refreshData()
    }

    fun closeTicket(
        ticket: TicketRevenue,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    closeTicket(
                        projectId = projectId,
                        ticket = ticket
                    )
                },
                onSuccess = { refreshData() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    fun deleteTicket(
        ticket: TicketRevenue,
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteTicket(
                        projectId = projectId,
                        ticket = ticket
                    )
                },
                onSuccess = { refreshData() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    override fun refreshData() {
        ticketsState.refresh()
        getProjectBalance()
    }

}