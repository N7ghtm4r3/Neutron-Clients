package com.tecknobit.neutron.ui.screens.project.presentation

import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseContent
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.project.data.TicketRevenue
import com.tecknobit.neutron.ui.screens.shared.data.ProjectRevenue
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `ProjectScreenViewModel` class is the support class used by the [com.tecknobit.neutron.ui.screens.project.presenter.ProjectScreen]
 *
 * @param projectId The identifier of the project displayed
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RevenueRelatedScreenViewModel
 */
class ProjectScreenViewModel(
    val projectId: String,
) : RevenueRelatedScreenViewModel() {

    /**
     *`_project` the current project displayed
     */
    private val _project = MutableStateFlow<ProjectRevenue?>(
        value = null
    )
    val project: StateFlow<ProjectRevenue?> = _project

    /**
     *`_balance` the current balance of the project displayed
     */
    private val _balance = MutableStateFlow(
        value = 0.0
    )
    val balance: StateFlow<Double> = _balance

    /**
     *`retrievePendingTickets` whether retrieve the pending tickets
     */
    private var retrievePendingTickets: Boolean = true

    /**
     *`retrieveClosedTickets` whether retrieve the closed tickets
     */
    private var retrieveClosedTickets: Boolean = true

    /**
     * Method to retrieve the project specified by the [projectId]
     */
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
                onFailure = { showSnackbarMessage(it) },
                onConnectionError = { notifyConnectionError() }
            )
        }
    }

    /**
     * Method to request the current balance of a project
     */
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

    /**
     *`ticketsState` the state used to manage the pagination of the tickets
     */
    val ticketsState = PaginationState<Int, TicketRevenue>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadTickets(
                page = page
            )
        }
    )

    /**
     * Method to load the tickets
     *
     * @param page The page to request
     */
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
                onConnectionError = { notifyConnectionError() }
            )
        }
    }

    /**
     * Method to apply the [retrievePendingTickets] filter
     *
     * @param retrieve whether retrieve or not the pending tickets
     */
    fun applyRetrievePendingTickets(
        retrieve: Boolean
    ) {
        retrievePendingTickets = retrieve
        refreshData()
    }

    /**
     * Method to apply the [retrieveClosedTickets] filter
     *
     * @param retrieve whether retrieve or not the closed tickets
     */
    fun applyRetrieveClosedTickets(
        retrieve: Boolean
    ) {
        retrieveClosedTickets = retrieve
        refreshData()
    }

    /**
     * Method request to close a [ticket]
     *
     * @param ticket The ticket to close
     */
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

    /**
     * Method request to delete a [ticket]
     *
     * @param ticket The ticket to delete
     */
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

    /**
     * Method to refresh the data after a filter applying or a revenue deletion
     */
    override fun refreshData() {
        ticketsState.refresh()
        getProjectBalance()
    }

    /**
     * Method to notify a connection error
     */
    override fun notifyConnectionError() {
        ticketsState.setError(Exception())
        setServerOfflineValue(true)
    }

    /**
     * Method to retrieve the information on the revenues and the wallet status after a connection
     * error
     */
    override fun retryAfterConnectionError() {
        ticketsState.retryLastFailedRequest()
    }
}