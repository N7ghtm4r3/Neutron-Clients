package com.tecknobit.neutron.ui.screens.insert.shared.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueDescriptionValid
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueTitleValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.LocalDateTime

@Structure
abstract class InsertScreenViewModel(
    private val revenueId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _revenue = MutableStateFlow<Revenue?>(
        value = null
    )
    val revenue : StateFlow<Revenue?> = _revenue

    lateinit var keyboardState: ScreenKeyboardState

    lateinit var addingGeneralRevenue: MutableState<Boolean>

    lateinit var title: MutableState<String>

    lateinit var titleError: MutableState<Boolean>

    lateinit var description: MutableState<String>

    lateinit var descriptionError: MutableState<Boolean>

    lateinit var insertionDate: MutableState<LocalDateTime>

    @RequiresSuperCall
    open fun retrieveRevenue() {
        if(revenueId == null)
            return
        // TODO: MAKE THE REQUEST THEN
        _revenue.value = GeneralRevenue.GeneralRevenueImpl(
            id = "id",
            title = "prova",
            value = 100.0,
            revenueDate = 1737055320000L,
            description = "gagagaga",
            labels = listOf(
                RevenueLabel(
                    id = "ga",
                    color = "#594DB6",
                    text = "ga"
                )
            )
        )
    }

    fun insert() {
        if(!isRevenueTitleValid(title.value)) {
            titleError.value = true
            return
        }
        if(addingGeneralRevenue.value && !isRevenueDescriptionValid(description.value)) {
            descriptionError.value = true
            return
        }
        insertRequest()
    }

    protected abstract fun insertRequest()

}