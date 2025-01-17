package com.tecknobit.neutron.helpers

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.neutroncore.enums.RevenuePeriod
import com.tecknobit.neutroncore.enums.RevenuePeriod.LAST_MONTH
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class PeriodFiltererViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _revenuePeriod = MutableStateFlow(
        value = LAST_MONTH
    )
    val revenuePeriod: StateFlow<RevenuePeriod> = _revenuePeriod

    fun applyRevenuePeriodFilter(
        revenuePeriod: RevenuePeriod,
        afterSet: () -> Unit
    ) {
        _revenuePeriod.value = revenuePeriod
        afterSet()
        refreshData()
    }

    abstract fun refreshData()

}