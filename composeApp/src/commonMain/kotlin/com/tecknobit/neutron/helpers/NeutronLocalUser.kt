package com.tecknobit.neutron.helpers

import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.neutroncore.CURRENCY_KEY
import com.tecknobit.neutroncore.enums.NeutronCurrency
import com.tecknobit.neutroncore.enums.NeutronCurrency.DOLLAR
import kotlinx.serialization.json.JsonObject

/**
 * The `NeutronLocalUser` class is useful to represent a user in the client application
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class NeutronLocalUser : EquinoxLocalUser(
    localStoragePath = "Neutron"
) {

    /**
     * `currency` the currency of the user
     */
    var currency: NeutronCurrency = DOLLAR
        set(value) {
            if (value != field) {
                setPreference(
                    key = CURRENCY_KEY,
                    value = value.name
                )
                field = value
            }
        }

    /**
     * Method to insert and initialize a new local user.
     *
     * @param hostAddress The host address with which the user communicates.
     * @param name The name of the user.
     * @param surname The surname of the user.
     * @param email The email address of the user.
     * @param password The password of the user.
     * @param language The preferred language of the user.
     * @param response The payload response received from an authentication request.
     * @param custom Custom parameters added during the customization of the equinox user
     */
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
            currencyName = custom.extractsCustomValue(
                itemPosition = 0
            )
        )
    }

    /**
     * Method to init the local user session
     */
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