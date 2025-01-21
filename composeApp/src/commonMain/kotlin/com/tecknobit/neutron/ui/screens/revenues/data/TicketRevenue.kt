package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.equinoxcore.time.TimeFormatter.daysUntil
import com.tecknobit.neutroncore.CLOSING_DATE_KEY
import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketRevenue(
    override val id: String,
    override val title: String,
    override val value: Double,
    @SerialName(REVENUE_DATE_KEY)
    override val revenueDate: Long,
    override val labels: List<RevenueLabel> = emptyList(),
    override val description: String,
    @SerialName(CLOSING_DATE_KEY)
    val closingDate: Long = -1,
) : GeneralRevenue {

    fun isPending() : Boolean {
        return closingDate == -1L
    }

    fun getTicketDuration() : Int {
        return revenueDate.daysUntil(
            closingDate
        )
    }

}