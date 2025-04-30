package com.tecknobit.neutron.ui.screens.shared.presentations

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseArrayData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.neutron.requester
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `RevenueLabelsRetriever` interface allows to retrieve the current labels of the user
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface RevenueLabelsRetriever {

    /**
     * Method to retrieve the current labels of the user
     *
     * @param labels The support list to load with the labels retrieved
     */
    fun retrieveUserLabels(
        labels: SnapshotStateList<RevenueLabel>,
    ) {
        MainScope().launch {
            requester.sendRequest(
                request = { getRevenuesLabels() },
                onSuccess = {
                    val supportLabelsList: List<RevenueLabel> = Json.decodeFromJsonElement(
                        json = it.toResponseArrayData()
                    )
                    labels.addAll(supportLabelsList)
                },
                onFailure = {}
            )
        }
    }

}