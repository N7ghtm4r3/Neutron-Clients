package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Revenue {
    val id: String
    val title: String
    val value: Double

    @SerialName(REVENUE_DATE_KEY)
    val revenueDate: Long

    @Serializable
    data class RevenueImpl(
        override val id: String,
        override val title: String,
        override val value: Double,
        override val revenueDate: Long,
    ) : Revenue

}
