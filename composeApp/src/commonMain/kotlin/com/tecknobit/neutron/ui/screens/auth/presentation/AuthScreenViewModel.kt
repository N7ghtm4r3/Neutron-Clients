package com.tecknobit.neutron.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxAuthViewModel
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.neutron.REVENUES_SCREEN
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.navigator
import com.tecknobit.neutron.requester
import com.tecknobit.neutroncore.CURRENCY_KEY
import kotlinx.serialization.json.JsonObject

/**
 * The `AuthScreenViewModel` class is the support class used to execute the authentication requests
 * to the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see EquinoxAuthViewModel
 */
class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    localUser = localUser,
    requester = requester
) {

    /**
     * Method to get the list of the custom parameters to use in the [signIn] request.
     *
     * The order of the custom parameters must be the same of that specified in your customization of the
     * [getQueryValuesKeys()](https://github.com/N7ghtm4r3/Equinox/blob/main/src/main/java/com/tecknobit/equinox/environment/helpers/services/EquinoxUsersHelper.java#L133)
     * method
     *
     **/
    @CustomParametersOrder(CURRENCY_KEY)
    override fun getSignInCustomParameters(): Array<out Any?> {
        return arrayOf(CURRENCY_KEY)
    }

    /**
     * Method to launch the application after the authentication request, will be instantiated with the user details
     * both the [requester] and the [localUser]
     *
     * @param response The response of the authentication request
     * @param name The name of the user
     * @param surname The surname of the user
     * @param language The language of the user
     * @param custom The custom parameters added in a customization of the equinox user
     */
    @RequiresSuperCall
    override fun launchApp(
        response: JsonObject,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?
    ) {
        super.launchApp(response, name, surname, language, *custom)
        navigator.navigate(REVENUES_SCREEN)
    }

}