package com.tecknobit.neutron.ui.screens.insert.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.neutron.helpers.RevenueLabelsRetriever
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueDescriptionValid
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueTitleValid
import kotlinx.datetime.LocalDateTime

class InsertRevenueScreenViewModel : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
), RevenueLabelsRetriever {

    lateinit var keyboardState: ScreenKeyboardState

    lateinit var addingGeneralRevenue: MutableState<Boolean>

    lateinit var title: MutableState<String>

    lateinit var titleError: MutableState<Boolean>

    lateinit var description: MutableState<String>

    lateinit var descriptionError: MutableState<Boolean>

    val labels = mutableStateListOf<RevenueLabel>()

    lateinit var insertionDate: MutableState<LocalDateTime>

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