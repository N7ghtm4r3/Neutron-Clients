@file:OptIn(ExperimentalUuidApi::class)

package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.neutroncore.REVENUE_LABEL_COLOR_KEY
import com.tecknobit.neutroncore.REVENUE_LABEL_TEXT_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * The `RevenueLabel` class is useful to represent a label attachable to a [GeneralRevenue]
 *
 * @property id The identifier of the label
 * @property text The text of the label
 * @property color The color of the label
 *
 * @author N7ghtm4r3 - Tecknobit
 */
@Serializable
data class RevenueLabel(
    val id: String = Uuid.random().toHexString(),
    @SerialName(REVENUE_LABEL_TEXT_KEY)
    val text: String,
    @SerialName(REVENUE_LABEL_COLOR_KEY)
    val color: String,
)
