package com.tecknobit.neutron.ui.screens.shared.data

import com.tecknobit.neutron.ui.screens.project.data.TicketRevenue
import com.tecknobit.neutron.ui.screens.shared.data.Revenue.RevenueImpl
import com.tecknobit.neutroncore.INITIAL_REVENUE_KEY
import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `ProjectRevenue` class is the represent a project revenue
 *
 * @property id The identifier of the revenue
 * @property title The title of the revenue
 * @property value The value of the revenue
 * @property revenueDate The date of the revenue
 * @property initialRevenue The initial revenue of the project
 * @property tickets The tickets attached to the project
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Revenue
 */
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