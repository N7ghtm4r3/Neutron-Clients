package com.tecknobit.neutron.helpers

import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.neutroncore.CURRENCY_KEY
import com.tecknobit.neutroncore.enums.NeutronCurrency
import kotlinx.serialization.json.JsonObject

class NeutronLocalUser : EquinoxLocalUser(
    localStoragePath = "Neutron"
) {

    var currency: NeutronCurrency? = null
        set(value) {
            setPreference(
                key = CURRENCY_KEY,
                value = value?.name
            )
            field = value
        }

    @RequiresSuperCall
    @CustomParametersOrder(CURRENCY_KEY)
    override fun insertNewUser(
        hostAddress: String,
        name: String,
        surname: String,
        email: String,
        password: String,
        language: String,
        response: JsonObject,
        vararg custom: Any?,
    ) {
        super.insertNewUser(
            hostAddress,
            name,
            surname,
            email,
            password,
            language,
            response,
            *custom
        )
        currency = NeutronCurrency.getInstance(
            currencyName = custom[0].toString()
        )
    }

    @RequiresSuperCall
    override fun initLocalUser() {
        super.initLocalUser()
        currency = NeutronCurrency.getInstance(
            currencyName = getPreference(
                key = CURRENCY_KEY
            )
        )
    }

}