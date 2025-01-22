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

/**
 * The `InsertScreenViewModel` class is the support class used to execute the authentication requests
 * to the backend
 *
 * @property revenueId The identifier of the revenue to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 */
@Structure
abstract class InsertScreenViewModel(
    private val revenueId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     * `_revenue` the existing revenue to edit
     */
    protected val _revenue = MutableStateFlow<Revenue?>(
        value = null
    )
    val revenue : StateFlow<Revenue?> = _revenue

    /**
     * `keyboardState` the state used to manage the amount value of the revenue
     */
    lateinit var keyboardState: ScreenKeyboardState

    /**
     * `addingGeneralRevenue` whether
     */
    lateinit var addingGeneralRevenue: MutableState<Boolean>

    /**
     * `title` the title of the revenue
     */
    lateinit var title: MutableState<String>

    /**
     * `titleError` whether the [title] field is not valid
     */
    lateinit var titleError: MutableState<Boolean>

    /**
     * `description` the description of the revenue
     */
    lateinit var description: MutableState<String>

    /**
     * `descriptionError` whether the [description] field is not valid
     */
    lateinit var descriptionError: MutableState<Boolean>

    /**
     * `insertionDate` the date of the insertion of the revenue
     */
    lateinit var insertionDate: MutableState<LocalDateTime>

    /**
     * Method to retrieve the revenue information to edit that revenue
     *
     * @param onSuccess The action to execute when the request has been successful
     */
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

    /**
     * Method to insert the created revenue validating before the information
     */
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

    /**
     * Method to execute the correct request to insert the revenue
     */
    protected abstract fun insertRequest()

    /**
     * Method to eventually review the application after has been inserted
     */
    protected fun onSuccessInsert() {
        val kReviewer = KReviewer()
        kReviewer.reviewInApp {
            navigator.goBack()
        }
    }

}