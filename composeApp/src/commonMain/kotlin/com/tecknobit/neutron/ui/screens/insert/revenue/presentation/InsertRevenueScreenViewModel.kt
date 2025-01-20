package com.tecknobit.neutron.ui.screens.insert.revenue.presentation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.insert.shared.presentation.InsertScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueLabelsRetriever
import kotlinx.coroutines.launch

class InsertRevenueScreenViewModel(
    revenueId: String?
) : InsertScreenViewModel(
    revenueId = revenueId
), RevenueLabelsRetriever {

    val labels = mutableStateListOf<RevenueLabel>()

    @RequiresSuperCall
    override fun retrieveRevenue() {
        super.retrieveRevenue()
        if(_revenue.value != null && _revenue.value is GeneralRevenue)
            labels.addAll((_revenue.value as GeneralRevenue).labels)
    }

    override fun insertRequest() {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    insertRevenue(
                        addingGeneralRevenue = addingGeneralRevenue.value,
                        title = title.value,
                        description = description.value,
                        value = keyboardState.parseAmount(),
                        revenueDate = insertionDate.value,
                        labels = labels
                    )
                },
                onSuccess = { onSuccessInsert() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}