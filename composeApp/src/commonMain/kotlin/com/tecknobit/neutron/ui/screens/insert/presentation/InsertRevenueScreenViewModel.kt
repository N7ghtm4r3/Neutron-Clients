package com.tecknobit.neutron.ui.screens.insert.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.neutron.helpers.RevenueLabelsRetriever
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueDescriptionValid
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueTitleValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

class InsertRevenueScreenViewModel(
    private val revenueId: String? = null
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), RevenueLabelsRetriever {

    private val _revenue = MutableStateFlow<Revenue?>(
        value = null
    )
    val revenue : StateFlow<Revenue?> = _revenue

    lateinit var keyboardState: ScreenKeyboardState

    lateinit var addingGeneralRevenue: MutableState<Boolean>

    lateinit var title: MutableState<String>

    lateinit var titleError: MutableState<Boolean>

    lateinit var description: MutableState<String>

    lateinit var descriptionError: MutableState<Boolean>

    val labels = mutableStateListOf<RevenueLabel>()

    lateinit var insertionDate: MutableState<LocalDateTime>

    fun retrieveRevenue() {
        if(revenueId == null)
            return
        // TODO: MAKE THE REQUEST THEN
        _revenue.value = GeneralRevenue.GeneralRevenueImpl(
            id = "id",
            title = "prova",
            value = 100.0,
            revenueDate = 1737055320000L,
            description = "gagagaga"
        )
        if(_revenue.value!! is GeneralRevenue)
            labels.addAll((_revenue.value!! as GeneralRevenue).labels)
    }

    fun insertRevenue() {
        if(!isRevenueTitleValid(title.value)) {
            titleError.value = true
            return
        }
        if(addingGeneralRevenue.value && !isRevenueDescriptionValid(description.value)) {
            descriptionError.value = true
            return
        }
        // TODO: MAKE THE REQUEST THEN
        navigator.goBack()
    }

}