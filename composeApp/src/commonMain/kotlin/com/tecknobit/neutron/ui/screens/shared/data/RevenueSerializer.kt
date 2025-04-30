package com.tecknobit.neutron.ui.screens.shared.data

import com.tecknobit.neutron.ui.screens.project.data.TicketRevenue
import com.tecknobit.neutroncore.CLOSING_DATE_KEY
import com.tecknobit.neutroncore.LABELS_KEY
import com.tecknobit.neutroncore.TICKETS_KEY
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

/**
 * The `RevenueSerializer` object is used as custom serializer to correctly serialize the different
 * revenues types
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see JsonContentPolymorphicSerializer
 */
object RevenueSerializer : JsonContentPolymorphicSerializer<Revenue>(Revenue::class) {

    /**
     * Determines a particular strategy for deserialization by looking on a parsed JSON [element].
     */
    override fun selectDeserializer(
        element: JsonElement
    ) = when {
        LABELS_KEY in element.jsonObject -> GeneralRevenue.GeneralRevenueImpl.serializer()
        CLOSING_DATE_KEY in element.jsonObject -> TicketRevenue.serializer()
        TICKETS_KEY in element.jsonObject -> ProjectRevenue.serializer()
        else -> Revenue.RevenueImpl.serializer()
    }

}