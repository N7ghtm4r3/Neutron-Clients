package com.tecknobit.neutron.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.requester
import com.tecknobit.neutroncore.enums.NeutronCurrency

class ProfileScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
    localUser = localUser
) {

    /**
     * `profilePic` -> the profile picture of the user
     */
    lateinit var currency: MutableState<NeutronCurrency>

    fun changeCurrency(
        onSuccess: () -> Unit,
    ) {
        // TODO: MAKE THE REQUEST THEN
        /*viewModelScope.launch {
            requester.sendRequest(
                request = {
                },
                onSuccess = {
                    localUser.currency = newCurrency
                    currency.value = newCurrency
                    onSuccess.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }*/
        localUser.currency = currency.value
        onSuccess.invoke()
    }

}