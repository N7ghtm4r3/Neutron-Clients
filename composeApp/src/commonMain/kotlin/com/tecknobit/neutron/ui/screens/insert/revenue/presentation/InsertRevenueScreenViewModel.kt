package com.tecknobit.neutron.ui.screens.insert.revenue.presentation

import androidx.compose.runtime.mutableStateListOf
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.neutron.helpers.KReviewer
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.ui.screens.insert.shared.presentation.InsertScreenViewModel
import com.tecknobit.neutron.ui.screens.revenues.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import com.tecknobit.neutron.ui.screens.shared.presentations.RevenueLabelsRetriever

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
        // TODO: MAKE THE REQUEST THEN
        // ON SUCCESS SCOPE
        val kReviewer = KReviewer()
        kReviewer.reviewInApp {
            navigator.goBack()
        }
    }

}