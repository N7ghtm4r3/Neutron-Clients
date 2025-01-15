package com.tecknobit.neutron.ui.screens.insert.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel

class InsertRevenueScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    lateinit var addingGeneralRevenue: MutableState<Boolean>

    lateinit var title: MutableState<String>

    lateinit var titleError: MutableState<Boolean>

    lateinit var description: MutableState<String>

    lateinit var descriptionError: MutableState<Boolean>

}