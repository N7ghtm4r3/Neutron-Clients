package com.tecknobit.neutron.ui.screens.revenues.data

import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object RevenueSerializer : JsonContentPolymorphicSerializer<Revenue>(Revenue::class) {

    override fun selectDeserializer(
        element: JsonElement
    ) = when {
        "labels" in element.jsonObject -> GeneralRevenue.GeneralRevenueImpl.serializer()
        "tickets" in element.jsonObject -> ProjectRevenue.serializer()
        "closingDate" in element.jsonObject -> TicketRevenue.serializer()
        else -> Revenue.RevenueImpl.serializer()
    }

}