package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.neutron.ui.screens.revenues.data.Revenue.RevenueImpl
import com.tecknobit.neutroncore.INITIAL_REVENUE_KEY
import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProjectRevenue(
    override val id: String,
    override val title: String,
    override val value: Double,
    @SerialName(REVENUE_DATE_KEY)
    override val revenueDate: Long,
    @SerialName(INITIAL_REVENUE_KEY)
    val initialRevenue: RevenueImpl,
    val tickets: List<TicketRevenue> = emptyList(),
) : Revenue