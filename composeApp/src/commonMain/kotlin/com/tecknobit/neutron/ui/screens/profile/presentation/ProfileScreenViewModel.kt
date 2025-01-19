package com.tecknobit.neutron.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.neutron.localUser
import com.tecknobit.neutron.requester
import com.tecknobit.neutroncore.enums.NeutronCurrency
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    changeCurrency(
                        newCurrency = currency.value
                    )
                },
                onSuccess = {
                    localUser.currency = currency.value
                    onSuccess.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}