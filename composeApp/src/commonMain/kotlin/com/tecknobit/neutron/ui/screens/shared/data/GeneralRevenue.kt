package com.tecknobit.neutron.ui.screens.shared.data

import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import com.tecknobit.neutroncore.REVENUE_DESCRIPTION_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `GeneralRevenue` interface is useful to give the basic details about a general revenue
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see Revenue
 */
interface GeneralRevenue : Revenue {

    /**
     *`id` the identifier of the revenue
     */
    override val id: String

    /**
     *`title` the title of the revenue
     */
    override val title: String

    /**
     *`value` the value of the revenue
     */
    override val value: Double

    /**
     *`revenueDate` the date of the revenue
     */
    override val revenueDate: Long

    /**
     *`labels` the labels attached to revenue
     */
    val labels: List<RevenueLabel>

    /**
     *`description` the description of the revenue
     */
    @SerialName(REVENUE_DESCRIPTION_KEY)
    val description: String

    /**
     * The `GeneralRevenueImpl` class is the default implementation of the [GeneralRevenue]
     *
     * @property id The identifier of the revenue
     * @property title The title of the revenue
     * @property value The value of the revenue
     * @property revenueDate The date of the revenue
     * @property labels The labels attached to revenue
     * @property description The description of the revenue
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     * @see Revenue
     * @see GeneralRevenue
     */
    @Serializable
    data class GeneralRevenueImpl(
        override val id: String,
        override val title: String,
        override val value: Double,
        @SerialName(REVENUE_DATE_KEY)
        override val revenueDate: Long,
        override val labels: List<RevenueLabel> = emptyList(),
        override val description: String = "",
    ) : GeneralRevenue

}