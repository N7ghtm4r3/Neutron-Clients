package com.tecknobit.neutron.ui.screens.shared.data

import com.tecknobit.neutroncore.REVENUE_DATE_KEY
import com.tecknobit.neutroncore.REVENUE_TITLE_KEY
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `Revenue` interface is useful to give the basic details about a revenue
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Polymorphic
interface Revenue {

    /**
     *`id` the identifier of the revenue
     */
    val id: String

    /**
     *`title` the title of the revenue
     */
    val title: String

    /**
     *`value` the value of the revenue
     */
    val value: Double

    /**
     *`revenueDate` the date of the revenue
     */
    val revenueDate: Long

    /**
     * The `RevenueImpl` class is the default implementation of the [Revenue]
     *
     * @property id The identifier of the revenue
     * @property title The title of the revenue
     * @property value The value of the revenue
     * @property revenueDate The date of the revenue
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     * @see Revenue
     */
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
