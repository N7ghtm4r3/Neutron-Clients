package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.neutroncore.REVENUE_DESCRIPTION_KEY
import com.tecknobit.neutroncore.REVENUE_LABELS_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface GeneralRevenue : Revenue {
    override val id: String
    override val title: String
    override val value: Double
    override val revenueDate: Long

    @SerialName(REVENUE_LABELS_KEY)
    val labels: List<RevenueLabel>

    @SerialName(REVENUE_DESCRIPTION_KEY)
    val description: String

    @Serializable
    data class GeneralImpl(
        override val id: String,
        override val title: String,
        override val value: Double,
        override val revenueDate: Long,
        override val labels: List<RevenueLabel> = emptyList(),
        override val description: String = "",
    ) : GeneralRevenue

}