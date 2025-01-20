package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import com.tecknobit.neutroncore.REVENUE_TITLE_KEY
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed interface Revenue {
    val id: String
    val title: String
    val value: Double
    val revenueDate: Long

    fun revenueDateAsString() : String {
        return revenueDate.toDateString()
    }

    @Serializable
    data class RevenueImpl(
        override val id: String,
        @SerialName(REVENUE_TITLE_KEY)
        override val title: String = "",
        override val value: Double,
        @SerialName(REVENUE_DATE_KEY)
        override val revenueDate: Long,
    ) : Revenue

}
