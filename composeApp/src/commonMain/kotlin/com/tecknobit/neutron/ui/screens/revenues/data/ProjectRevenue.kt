package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.neutron.ui.screens.revenues.data.Revenue.RevenueImpl
import kotlinx.serialization.Serializable

@Serializable
data class ProjectRevenue(
    override val id: String,
    override val title: String,
    override val value: Double,
    override val revenueDate: Long,
    val initialRevenue: RevenueImpl,
    val tickets: List<TicketRevenue>,
) : Revenue