package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.neutroncore.REVENUE_LABEL_COLOR_KEY
import com.tecknobit.neutroncore.REVENUE_LABEL_TEXT_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RevenueLabel(
    val id: String,
    @SerialName(REVENUE_LABEL_TEXT_KEY)
    val text: String,
    @SerialName(REVENUE_LABEL_COLOR_KEY)
    val color: String,
)
