package com.tecknobit.neutron.ui.screens.revenues.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setHasBeenDisconnectedValue
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendPaginatedRequest
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.shared.data.Revenue
import com.tecknobit.neutron.ui.screens.shared.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.shared.data.RevenueSerializer
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueLabelsRetriever
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueRelatedScreenViewModel
import com.tecknobit.neutroncore.dtos.WalletStatus
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `RevenuesScreenViewModel` class is the support class used by the [com.tecknobit.neutron.ui.screens.revenues.presenter.RevenuesScreen]
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RevenueRelatedScreenViewModel
 * @see RevenueLabelsRetriever
 */
class RevenuesScreenViewModel : RevenueRelatedScreenViewModel() , RevenueLabelsRetriever {

    /**
     *`_walletStatus` the current status of the wallet
     */
    private val _walletStatus = MutableStateFlow<WalletStatus?>(
        value = null
    )
    val walletStatus: StateFlow<WalletStatus?> = _walletStatus

    /**
     *`labelsFilter` the list of the labels to use as filters
     */
    var labelsFilter = mutableStateListOf<RevenueLabel>()

    /**
     *`retrieveGeneralRevenues` whether retrieve the general revenues
     */
    private var retrieveGeneralRevenues: Boolean = true

    /**
     *`retrieveProjectsRevenues` whether retrieve the projects revenues
     */
    private var retrieveProjectsRevenues: Boolean = true

    /**
     * Method to request the current status of the wallet
     */
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

    /**
     * Method to apply the labels filters
     *
     * @param onApply The action to execute when the filters have been applied
     */
    fun applyLabelsFilters(
        onApply: () -> Unit
    ) {
        refreshData()
        onApply()
    }

    /**
     * Method to apply the [retrieveGeneralRevenues] filter
     *
     * @param retrieve whether retrieve or not the general revenues
     */
    fun applyRetrieveGeneralRevenuesFilter(
        retrieve: Boolean
    ) {
        retrieveGeneralRevenues = retrieve
        refreshData()
    }

    /**
     * Method to apply the [retrieveProjectsRevenues] filter
     *
     * @param retrieve whether retrieve or not the projects revenues
     */
    fun applyRetrieveProjectsFilter(
        retrieve: Boolean
    ) {
        retrieveProjectsRevenues = retrieve
        refreshData()
    }

    /**
     *`revenuesState` the state used to manage the pagination of the revenues
     */
    val revenuesState = PaginationState<Int, Revenue>(
        initialPageKey = PaginatedResponse.DEFAULT_PAGE,
        onRequestPage = { page ->
            loadRevenues(
                page = page
            )
        }
    )

    /**
     * Method to load the revenues
     *
     * @param page The page to request
     */
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

    /**
     * Method to refresh the data after a filter applying or a revenue deletion
     */
    override fun refreshData() {
        revenuesState.refresh()
        getWalletStatus()
    }

}