package com.tecknobit.neutron.ui.screens.insert.shared.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.neutron.helpers.KReviewer
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.components.screenkeyboard.ScreenKeyboardState
import com.tecknobit.neutron.ui.screens.revenues.data.Revenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueSerializer
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueDescriptionValid
import com.tecknobit.neutroncore.helpers.NeutronInputsValidator.isRevenueTitleValid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

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
    open fun retrieveRevenue(
        onSuccess: (() -> Unit)? = null,
    ) {
        if(revenueId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getRevenue(
                        revenueId = revenueId
                    )
                },
                onSuccess = {
                    _revenue.value = Json.decodeFromJsonElement(
                        deserializer = RevenueSerializer,
                        element = it.toResponseData()
                    )
                    onSuccess?.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
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

    protected fun onSuccessInsert() {
        val kReviewer = KReviewer()
        kReviewer.reviewInApp {
            navigator.goBack()
        }
    }

}