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

class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    localUser = localUser,
    requester = requester
) {

    @CustomParametersOrder(CURRENCY_KEY)
    override fun getSignInCustomParameters(): Array<out Any?> {
        return arrayOf(CURRENCY_KEY)
    }

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