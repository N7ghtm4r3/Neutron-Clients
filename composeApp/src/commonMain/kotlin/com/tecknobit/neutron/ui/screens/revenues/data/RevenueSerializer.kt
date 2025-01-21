package com.tecknobit.neutron.ui.screens.revenues.data

import com.tecknobit.neutroncore.CLOSING_DATE_KEY
import com.tecknobit.neutroncore.LABELS_KEY
import com.tecknobit.neutroncore.TICKETS_KEY
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object RevenueSerializer : JsonContentPolymorphicSerializer<Revenue>(Revenue::class) {

    override fun selectDeserializer(
        element: JsonElement
    ) = when {
        LABELS_KEY in element.jsonObject -> GeneralRevenue.GeneralRevenueImpl.serializer()
        CLOSING_DATE_KEY in element.jsonObject -> TicketRevenue.serializer()
        TICKETS_KEY in element.jsonObject -> ProjectRevenue.serializer()
        else -> Revenue.RevenueImpl.serializer()
    }

}