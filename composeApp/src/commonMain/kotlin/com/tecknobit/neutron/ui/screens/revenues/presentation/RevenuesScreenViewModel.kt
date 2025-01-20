package com.tecknobit.neutron.ui.screens.revenues.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueSerializer
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueLabelsRetriever
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel
import com.tecknobit.neutroncore.dtos.WalletStatus
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class RevenuesScreenViewModel : RevenueRelatedScreenViewModel() , RevenueLabelsRetriever {

    private val _walletStatus = MutableStateFlow<WalletStatus?>(
        value = null
    )
    val walletStatus: StateFlow<WalletStatus?> = _walletStatus

    var labelsFilter = mutableStateListOf<RevenueLabel>()

    private var retrieveGeneralRevenues: Boolean = true

    private var retrieveProjectsRevenues: Boolean = true

    fun getWalletStatus() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getWalletStatus(
                        period = _revenuePeriod.value,
                        labels = labelsFilter,
                        retrieveGeneralRevenues = retrieveGeneralRevenues,
                        retrieveProjectsRevenues = retrieveProjectsRevenues
                    )
                },
                onSuccess = { response ->
                    _walletStatus.value = Json.decodeFromJsonElement(response.toResponseData())
                    setServerOfflineValue(false)
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    fun applyLabelsFilters(
        onApply: () -> Unit
    ) {
        refreshData()
        onApply()
    }

    fun applyRetrieveGeneralRevenuesFilter(
        retrieve: Boolean
    ) {
        retrieveGeneralRevenues = retrieve
        refreshData()
    }

    fun applyRetrieveProjectsFilter(
        retrieve: Boolean
    ) {
        retrieveProjectsRevenues = retrieve
        refreshData()
    }

    val revenuesState = PaginationState<Int, Revenue>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadRevenues(
                page = page
            )
        }
    )

    private fun loadRevenues(
        page: Int,
    ) {
        viewModelScope.launch {
            requester.sendPaginatedRequest(
                request = {
                    getRevenues(
                        page = page,
                        period = _revenuePeriod.value,
                        labels = labelsFilter,
                        retrieveGeneralRevenues = retrieveGeneralRevenues,
                        retrieveProjectsRevenues = retrieveProjectsRevenues
                    )
                },
                serializer = RevenueSerializer,
                onSuccess = { paginatedResponse ->
                    revenuesState.appendPage(
                        items = paginatedResponse.data,
                        nextPageKey = paginatedResponse.nextPage,
                        isLastPage = paginatedResponse.isLastPage
                    )
                    setServerOfflineValue(false)
                },
                onFailure = { setHasBeenDisconnectedValue(true) },
                onConnectionError = { setServerOfflineValue(true) }
            )
        }
    }

    override fun refreshData() {
        revenuesState.refresh()
        getWalletStatus()
    }

}