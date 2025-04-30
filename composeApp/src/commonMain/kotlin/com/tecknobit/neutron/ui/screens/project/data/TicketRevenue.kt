package com.tecknobit.neutron.ui.screens.project.data

import com.tecknobit.equinoxcore.time.TimeFormatter.daysUntil
import com.tecknobit.neutron.ui.screens.shared.data.GeneralRevenue
import com.tecknobit.neutron.ui.screens.shared.data.RevenueLabel
import com.tecknobit.neutroncore.CLOSING_DATE_KEY
import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `TicketRevenue` class is useful to represent a ticket revenue
 *
 * @property id The identifier of the revenue
 * @property title The title of the revenue
 * @property value The value of the revenue
 * @property revenueDate The date of the revenue
 * @property labels The labels attached to revenue
 * @property description The description of the revenue
 * @property closingDate When the ticket has been completed
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Revenue
 * @see GeneralRevenue
 */
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

    /**
     * Method to check whether the ticket is still pending
     *
     * @return whether the ticket is pending as [Boolean]
     */
    fun isPending() : Boolean {
        return closingDate == -1L
    }

    /**
     * Method to get the temporal gap between when the ticket has been opened and when it has been
     * closed
     *
     * @return the temporal gap as [Int]
     */
    fun getTicketDuration() : Int {
        return revenueDate.daysUntil(
            closingDate
        )
    }

}