package com.tecknobit.neutron.ui.screens.shared.presentations

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_MONTH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class RevenueRelatedScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _revenuePeriod = MutableStateFlow(
        value = LAST_MONTH
    )
    val revenuePeriod: StateFlow<RevenuePeriod> = _revenuePeriod

    protected val _hideBalances = MutableStateFlow(
        value = false
    )
    val hideBalances: StateFlow<Boolean> = _hideBalances

    fun manageBalancesVisibility() {
        _hideBalances.value = !_hideBalances.value
    }

    fun applyRevenuePeriodFilter(
        revenuePeriod: RevenuePeriod,
        afterSet: () -> Unit
    ) {
        _revenuePeriod.value = revenuePeriod
        afterSet()
        refreshData()
    }

    fun deleteRevenue(
        revenue: Revenue,
        onDelete: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onDelete.invoke()
    }

    abstract fun refreshData()

}