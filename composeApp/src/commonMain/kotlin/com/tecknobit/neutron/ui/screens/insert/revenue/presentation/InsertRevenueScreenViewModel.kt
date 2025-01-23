package com.tecknobit.neutron.ui.screens.insert.revenue.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.neutron.helpers.KReviewer
import com.tecknobit.neutron.helpers.mergeIfNotContained
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.insert.shared.presentation.InsertScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueLabelsRetriever
import kotlinx.coroutines.launch

/**
 * The `InsertRevenueScreenViewModel` class is the support class used to create a new revenue or
 * edit an exiting one
 *
 * @property revenueId The identifier of the revenue to edit
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see InsertScreenViewModel
 * @see RevenueLabelsRetriever
 */
class InsertRevenueScreenViewModel(
    revenueId: String?
) : InsertScreenViewModel(
    revenueId = revenueId
), RevenueLabelsRetriever {

    /**
     * `labels` the labels attached to the revenue
     */
    val labels = mutableStateListOf<RevenueLabel>()

    /**
     * Method to retrieve the revenue information to edit that revenue
     *
     * @param onSuccess The action to execute when the request has been successful
     */
    @RequiresSuperCall
    override fun retrieveRevenue(
        onSuccess: (() -> Unit)?,
    ) {
        super.retrieveRevenue(
            onSuccess = {
                if (_revenue.value != null && _revenue.value is GeneralRevenue)
                    labels.mergeIfNotContained((_revenue.value as GeneralRevenue).labels)
            }
        )
    }

    /**
     * Method to execute the correct request to insert the revenue
     */
    override fun insertRequest() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    insertRevenue(
                        addingGeneralRevenue = addingGeneralRevenue.value,
                        revenue = _revenue.value,
                        title = title.value,
                        description = description.value,
                        value = keyboardState.parseAmount(),
                        revenueDate = insertionDate.value,
                        labels = labels
                    )
                },
                onSuccess = {
                    val kReviewer = KReviewer()
                    kReviewer.reviewInApp {
                        navigator.goBack()
                    }
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}