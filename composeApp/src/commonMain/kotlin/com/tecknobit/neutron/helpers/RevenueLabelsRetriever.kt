package com.tecknobit.neutron.helpers

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxcompose.utilities.generateRandomColor
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.neutron.ui.screens.revenues.data.RevenueLabel
import kotlin.random.Random

interface RevenueLabelsRetriever {

    fun retrieveUserLabels() : SnapshotStateList<RevenueLabel> {
        // TODO: MAKE THE REQUEST THEN
        val currentUserLabels = mutableStateListOf<RevenueLabel>()
        for (j in 0 until Random.nextInt(10)) {
            currentUserLabels.add(
                RevenueLabel(
                    id = Random.nextLong().toString(),
                    text = "RevenueLabelBadge #$j",
                    color = generateRandomColor().toHex()
                )
            )
        }
        return currentUserLabels
    }

}