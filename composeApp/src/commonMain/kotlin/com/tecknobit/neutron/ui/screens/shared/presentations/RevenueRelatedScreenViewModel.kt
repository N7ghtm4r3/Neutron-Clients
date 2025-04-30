package com.tecknobit.neutron.ui.screens.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_MONTH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * The `RevenueRelatedScreenViewModel` class is the support class used to help the screens which
 * manage the revenue items
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
@Structure
abstract class RevenueRelatedScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `_revenuePeriod` the current period of the revenues displayed
     */
    protected val _revenuePeriod = MutableStateFlow(
        value = LAST_MONTH
    )
    val revenuePeriod: StateFlow<RevenuePeriod> = _revenuePeriod

    /**
     * `_hideBalances` whether to hide the balances value
     */
    protected val _hideBalances = MutableStateFlow(
        value = false
    )
    val hideBalances: StateFlow<Boolean> = _hideBalances

    /**
     * Method to manage the [_hideBalances] state
     */
    fun manageBalancesVisibility() {
        _hideBalances.value = !_hideBalances.value
    }

    /**
     * Method to apply the period filter to the revenues retrieving
     *
     * @param revenuePeriod The value to apply
     * @param afterSet The action to execute after the value has been set
     */
    fun applyRevenuePeriodFilter(
        revenuePeriod: RevenuePeriod,
        afterSet: () -> Unit
    ) {
        _revenuePeriod.value = revenuePeriod
        afterSet()
        refreshData()
    }

    /**
     * Method to delete a revenue
     *
     * @param revenue The revenue to delete
     * @param onDelete The action to execute when the revenue has been deleted
     */
    fun deleteRevenue(
        revenue: Revenue,
        onDelete: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    deleteRevenue(
                        revenue = revenue
                    )
                },
                onSuccess = {
                    onDelete.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to refresh the data after a filter applying or a revenue deletion
     */
    abstract fun refreshData()

}