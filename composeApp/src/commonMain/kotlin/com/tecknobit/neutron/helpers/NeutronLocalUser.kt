package com.tecknobit.neutron.helpers

import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.helpers.THEME_KEY
import com.tecknobit.neutron.NeutronConfig.LOCAL_STORAGE_PATH
import com.tecknobit.neutroncore.CURRENCY_KEY
import com.tecknobit.neutroncore.enums.NeutronCurrency
import com.tecknobit.neutroncore.enums.NeutronCurrency.DOLLAR

/**
 * The `NeutronLocalUser` class is useful to represent a user in the client application
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class NeutronLocalUser : EquinoxLocalUser(
    localStoragePath = LOCAL_STORAGE_PATH,
    observableKeys = setOf(THEME_KEY)
) {

    /**
     * `currency` The currency of the user
     */
    var currency: NeutronCurrency = DOLLAR
        private set

    /**
     * Method used to insert a new user and save locally his/her properties
     *
     * @param hostAddress The host address with which the user communicates
     * @param userId The identifier of the user
     * @param userToken The token of the user
     * @param profilePic The profile picture of the user
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param language The language of the user
     * @param custom The custom parameters added during the customization of the [EquinoxLocalUser]
     */
    @RequiresSuperCall
    @CustomParametersOrder(CURRENCY_KEY)
    override fun insertNewUser(
        hostAddress: String,
        userId: String,
        userToken: String,
        profilePic: String,
        name: String,
        surname: String,
        email: String,
        language: String,
        vararg custom: Any?,
    ) {
        super.insertNewUser(
            hostAddress,
            userId,
            userToken,
            profilePic,
            name,
            surname,
            email,
            language,
            *custom
        )
        val currency = NeutronCurrency.getInstance(
            currencyName = custom.extractsCustomValue(
                itemPosition = 0
            )
        )
        initCurrency(
            currency = currency
        )
    }

    /**
     * Method to initialize the [currency] property and locally save its value with the [savePreference] method
     *
     * @param currency The currency of the user
     *
     * @since 1.0.5
     */
    fun initCurrency(
        currency: NeutronCurrency,
    ) {
        this.currency = currency
        savePreference(
            key = CURRENCY_KEY,
            value = currency
        )
    }

    /**
     * Method to init the local user session
     */
    @RequiresSuperCall
    override fun initLocalUser() {
        super.initLocalUser()
        setNullSafePreference(
            key = CURRENCY_KEY,
            defPrefValue = DOLLAR,
            prefInit = { currency ->
                this.currency = currency
            }
        )
    }

}